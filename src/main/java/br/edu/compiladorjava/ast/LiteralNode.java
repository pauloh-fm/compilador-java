package br.edu.compiladorjava.ast;

public class LiteralNode extends AstNode {
    private String value;
    private String type;

    public LiteralNode(String value, String type, int line, int column) {
        super(line, column);
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visitLiteral(this);
    }
}
