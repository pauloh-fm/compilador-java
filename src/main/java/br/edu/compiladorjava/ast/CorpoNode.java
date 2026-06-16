package br.edu.compiladorjava.ast;

public class CorpoNode extends AstNode {
    private DeclarationsNode declarations;
    private CommandBlockNode commandBlock;

    public CorpoNode(DeclarationsNode declarations, CommandBlockNode commandBlock, int line, int column) {
        super(line, column);
        this.declarations = declarations;
        this.commandBlock = commandBlock;
    }

    public DeclarationsNode getDeclarations() {
        return declarations;
    }

    public CommandBlockNode getCommandBlock() {
        return commandBlock;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitCorpo(this);
    }
}
