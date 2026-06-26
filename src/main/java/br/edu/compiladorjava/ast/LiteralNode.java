package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class LiteralNode extends ExpressionNode {
    public String value;

    public LiteralNode(String value, int line, int column) {
        this.value = value;
        this.line = line;
        this.column = column;
    }

    @Override
    public void visit(Visitor v) {
        v.visitLiteralNode(this);
    }
}