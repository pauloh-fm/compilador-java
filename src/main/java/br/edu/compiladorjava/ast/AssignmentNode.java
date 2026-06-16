package br.edu.compiladorjava.ast;

public class AssignmentNode extends AstNode {
    private String variable;
    private AstNode expression;

    public AssignmentNode(String variable, AstNode expression, int line, int column) {
        super(line, column);
        this.variable = variable;
        this.expression = expression;
    }

    public String getVariable() {
        return variable;
    }

    public AstNode getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitAssignment(this);
    }
}
