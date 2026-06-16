package br.edu.compiladorjava.ast;

import java.util.List;

public class CommandBlockNode extends AstNode {
    private List<AstNode> commands;

    public CommandBlockNode(List<AstNode> commands, int line, int column) {
        super(line, column);
        this.commands = commands;
    }

    public List<AstNode> getCommands() {
        return commands;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitCommandBlock(this);
    }
}
