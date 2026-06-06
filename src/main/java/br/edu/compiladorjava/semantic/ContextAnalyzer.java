package br.edu.compiladorjava.semantic;

import br.edu.compiladorjava.ast.*;
import br.edu.compiladorjava.visitor.Visitor;

public class ContextAnalyzer implements Visitor {
    private final SymbolTable table = new SymbolTable();
    private Type currentType;
    public void analyze(ProgramNode program) {
        program.visit(this);
    }

    private void error(String message, AstNode node) {
        throw new SemanticException(message, node.line, node.column);
    }

    private Type convertType(String t) {
        if (t.equals("integer")) return Type.INTEGER;
        if (t.equals("boolean")) return Type.BOOLEAN;
        return Type.ERROR;
    }

    @Override
    public void visitProgramNode(ProgramNode p) {
        if (p.declarations != null) {
            p.declarations.visit(this);
        }

        if (p.commands != null) {
            p.commands.visit(this);
        }
    }

    @Override
    public void visitDeclarationNode(DeclarationNode d) {
        DeclarationNode current = d;
        while (current != null) {
            Symbol s = new Symbol(current.name, convertType(current.type),
                    table.currentScopeLevel());
            if (!table.insert(s)) {
                error("Variável '" + current.name + "' já declarada neste escopo", current);
            }
            current = current.next;
        }
    }

    @Override
    public void visitAssignmentNode(AssignmentNode a) {

        AssignmentNode current = a;

        while (current != null) {

            Symbol s = table.retrieve(current.variable);

            if (s == null) {
                error(
                        "Variável '" + current.variable +
                                "' não foi declarada",
                        current
                );
            }

            current.expression.visit(this);

            Type exprType = currentType;

            if (s.getType() != exprType) {

                error(
                        "Não é possível atribuir "
                                + exprType
                                + " a uma variável "
                                + s.getType(),
                        current
                );
            }

            current = (AssignmentNode) current.next;
        }
    }

    @Override
    public void visitIdentifierNode(IdentifierNode i) {
        Symbol s = table.retrieve(i.name);
        if (s == null) {
            error("Variável '" + i.name + "' não foi declarada", i);
        }
        currentType = s.getType();
    }

    @Override
    public void visitLiteralNode(LiteralNode l) {
        if (l.value.equals("true") || l.value.equals("false")) {
            currentType = Type.BOOLEAN;
        }
        else {
            currentType = Type.INTEGER;
        }
    }

    @Override
    public void visitBinaryExpressionNode(BinaryExpressionNode e) {
        e.left.visit(this);
        Type left = currentType;

        e.right.visit(this);
        Type right = currentType;

        switch (e.operator) {
            case "+":
            case "-":
            case "*":
            case "/":
                if (left != Type.INTEGER || right != Type.INTEGER) {
                    error("Operador '" + e.operator + "' requer operandos INTEGER", e);
                }
                currentType = Type.INTEGER;
                break;

            case "and":
            case "or":
                if (left != Type.BOOLEAN || right != Type.BOOLEAN) {
                    error("Operador '" + e.operator + "' requer operandos BOOLEAN", e);
                }
                currentType = Type.BOOLEAN;
                break;

            case "<":
            case ">":
            case "=":
                if (left != right) {
                    error("Não é possível comparar " + left + " com " + right, e);
                }
                currentType = Type.BOOLEAN;
                break;
        }
    }
}
