package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class BinaryExpressionNode extends ExpressionNode {

    public String operator;
    public ExpressionNode left;
    public ExpressionNode right;

    public BinaryExpressionNode(String operator, ExpressionNode left, ExpressionNode right, int line, int column) {
        this.operator = operator;
        this.left = left;
        this.right = right;
        this.line = line;
        this.column = column;
    }

    @Override
    public void visit(Visitor v) {
        v.visitBinaryExpressionNode(this);
    }
}