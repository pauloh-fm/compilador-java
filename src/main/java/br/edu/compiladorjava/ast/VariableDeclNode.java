package br.edu.compiladorjava.ast;

public class VariableDeclNode extends AstNode {
    private String name;
    private String type;

    public VariableDeclNode(String name, String type, int line, int column) {
        super(line, column);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVariableDecl(this);
    }
}
