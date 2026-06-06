package br.edu.compiladorjava.semantic;

import java.util.*;

public class SymbolTable {
    private final Stack<Map<String, Symbol>> scopes = new Stack<>();

    public SymbolTable() {
        openScope();
    }

    public void openScope() {
        scopes.push(new HashMap<>());
    }

    public void closeScope() {
        scopes.pop();
    }

    public boolean insert(Symbol symbol) {
        Map<String, Symbol> current = scopes.peek();

        if (current.containsKey(symbol.getName()))
            return false;

        current.put(symbol.getName(), symbol);

        return true;
    }

    public Symbol retrieve(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Symbol s = scopes.get(i).get(name);
            if (s != null)
                return s;
        }
        return null;
    }

    public int currentScopeLevel() {
        return scopes.size() - 1;
    }
}