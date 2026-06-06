package br.edu.compiladorjava.lexer;

public class Token {
    public final Kind kind;
    public final String lexeme;
    public final int line;
    public final int column;

    public Token(Kind kind, String lexeme, int line, int column) {

        // resolve palavras reservadas
        if (kind == Kind.IDENTIFIER) {
            Kind reserved = Kind.fromString(lexeme);
            if (reserved != null) {
                this.kind = reserved;
                this.lexeme = reserved.getSpelling();
                this.line = line;
                this.column = column;
                return;
            }
        }
        this.kind = kind;
        if (kind == Kind.EOT) {
            this.lexeme = "<eot>";
        } else if (kind == Kind.IDENTIFIER ||
                kind == Kind.INTLITERAL) {
            this.lexeme = lexeme;
        } else {
            this.lexeme = kind.getSpelling();
        }
        this.line = line;
        this.column = column;
    }

    public int getType() {
        return kind.ordinal();
    }

    @Override
    public String toString() {
        return String.format("%-10d %-20s %-10d %-10d", getType(), lexeme, line, column);
    }
}