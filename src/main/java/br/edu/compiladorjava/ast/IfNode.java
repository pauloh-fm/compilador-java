package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class IfNode extends CommandNode {
    public final ExpressionNode condition;
    public final CommandNode thenBranch;
    public final CommandNode elseBranch;

    public IfNode(ExpressionNode condition, CommandNode thenBranch, CommandNode elseBranch, int line, int column) {
        this.line = line;
        this.column = column;
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitIfNode(this);
    }
}
