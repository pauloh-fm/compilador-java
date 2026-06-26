package br.edu.compiladorjava.semantic;

public class Symbol {

    private final String name;
    private final Type type;
    private final int scope;

    public Symbol(String name, Type type, int scope) {
        this.name = name;
        this.type = type;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getScope() {
        return scope;
    }
}