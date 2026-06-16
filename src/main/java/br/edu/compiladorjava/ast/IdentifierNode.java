package br.edu.compiladorjava.ast;

public class IdentifierNode extends AstNode {
    private String name;

    public IdentifierNode(String name, int line, int column) {
        super(line, column);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitIdentifier(this);
    }
}
