package br.edu.compiladorjava.ast;

public class UnaryOpNode extends AstNode {
    private String operator;
    private AstNode operand;

    public UnaryOpNode(String operator, AstNode operand, int line, int column) {
        super(line, column);
        this.operator = operator;
        this.operand = operand;
    }

    public String getOperator() {
        return operator;
    }

    public AstNode getOperand() {
        return operand;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitUnaryOp(this);
    }
}
