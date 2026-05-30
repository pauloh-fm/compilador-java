package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class IdentifierNode extends ExpressionNode {

    public String name;

    public IdentifierNode(String name) {
        this.name = name;
    }

    @Override
    public void visit(Visitor v) {
        v.visitIdentifierNode(this);
    }
}
