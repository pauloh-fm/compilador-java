package br.edu.compiladorjava.ast;

public class ProgramNode extends AstNode {
    private String name;
    private CorpoNode corpo;

    public ProgramNode(String name, CorpoNode corpo, int line, int column) {
        super(line, column);
        this.name = name;
        this.corpo = corpo;
    }

    public String getName() {
        return name;
    }

    public CorpoNode getCorpo() {
        return corpo;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitProgram(this);
    }
}
