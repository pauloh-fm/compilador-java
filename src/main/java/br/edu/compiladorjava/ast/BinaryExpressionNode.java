package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class BinaryExpressionNode extends ExpressionNode {

    public String operator;

    public ExpressionNode left;
    public ExpressionNode right;

    public BinaryExpressionNode(
            String operator,
            ExpressionNode left,
            ExpressionNode right) {

        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public void visit(Visitor v) {
        v.visitBinaryExpressionNode(this);
    }
}