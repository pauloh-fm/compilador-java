package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class LiteralNode extends ExpressionNode {

    public String value;

    public LiteralNode(String value) {
        this.value = value;
    }

    @Override
    public void visit(Visitor v) {
        v.visitLiteralNode(this);
    }
}