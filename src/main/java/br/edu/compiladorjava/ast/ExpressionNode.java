package br.edu.compiladorjava.ast;

public class ExpressionNode extends AstNode {
    private AstNode left;

    public ExpressionNode(AstNode left, int line, int column) {
        super(line, column);
        this.left = left;
    }

    public AstNode getLeft() {
        return left;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitExpression(this);
    }
}
