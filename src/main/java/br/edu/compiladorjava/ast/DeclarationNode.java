package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class DeclarationNode extends AstNode {
    public String name;
    public String type;
    public DeclarationNode next;

    public DeclarationNode(String name, String type, int line, int column) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.column = column;
    }

    @Override
    public void visit(Visitor v) {
        v.visitDeclarationNode(this);
    }
}