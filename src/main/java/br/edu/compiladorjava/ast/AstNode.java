package br.edu.compiladorjava.ast;

public abstract class AstNode {
    protected int line;
    protected int column;

    public AstNode(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public abstract <T> T accept(AstVisitor<T> visitor);
}
