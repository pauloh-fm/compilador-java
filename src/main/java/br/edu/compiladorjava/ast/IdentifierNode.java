package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class IdentifierNode extends ExpressionNode {
    public String name;

    public IdentifierNode(String name, int line, int column) {
        this.name = name;
        this.line = line;
        this.column = column;
    }

    @Override
    public void visit(Visitor v) {
        v.visitIdentifierNode(this);
    }
}
