package br.edu.compiladorjava.ast;

public class BinaryOpNode extends AstNode {
    private AstNode left;
    private String operator;
    private AstNode right;

    public BinaryOpNode(AstNode left, String operator, AstNode right, int line, int column) {
        super(line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public AstNode getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public AstNode getRight() {
        return right;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitBinaryOp(this);
    }
}
