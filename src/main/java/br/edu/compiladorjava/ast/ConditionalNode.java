package br.edu.compiladorjava.ast;

public class ConditionalNode extends AstNode {
    private AstNode condition;
    private AstNode thenCommand;
    private AstNode elseCommand;

    public ConditionalNode(AstNode condition, AstNode thenCommand, AstNode elseCommand, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.thenCommand = thenCommand;
        this.elseCommand = elseCommand;
    }

    public AstNode getCondition() {
        return condition;
    }

    public AstNode getThenCommand() {
        return thenCommand;
    }

    public AstNode getElseCommand() {
        return elseCommand;
    }

    public boolean hasElse() {
        return elseCommand != null;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitConditional(this);
    }
}
