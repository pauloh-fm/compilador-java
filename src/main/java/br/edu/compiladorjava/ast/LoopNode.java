package br.edu.compiladorjava.ast;

public class LoopNode extends AstNode {
    private AstNode condition;
    private AstNode command;

    public LoopNode(AstNode condition, AstNode command, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.command = command;
    }

    public AstNode getCondition() {
        return condition;
    }

    public AstNode getCommand() {
        return command;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitLoop(this);
    }
}
