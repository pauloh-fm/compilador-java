package br.edu.compiladorjava.ast;

public class VariableNode extends AstNode {
    private String name;

    public VariableNode(String name, int line, int column) {
        super(line, column);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitVariable(this);
    }
}
