package br.edu.compiladorjava.ast;

import br.edu.compiladorjava.visitor.Visitor;

public class WhileNode extends CommandNode {
    public final ExpressionNode condition;
    public final CommandNode body;

    public WhileNode(ExpressionNode condition, CommandNode body, int line, int column) {
        this.line = line;
        this.column = column;
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visitWhileNode(this);
    }
}
