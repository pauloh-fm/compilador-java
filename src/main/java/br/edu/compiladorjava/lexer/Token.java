package br.edu.compiladorjava.lexer;

public class Token {

    public final Kind kind;
    public final String lexeme; // só usado para IDENTIFIER e INTLITERAL

    public Token(Kind kind, String lexeme) {

        // 🔥 resolve palavras reservadas automaticamente
        if (kind == Kind.IDENTIFIER) {
            Kind reserved = Kind.fromString(lexeme);
            if (reserved != null) {
                this.kind = reserved;
                this.lexeme = null;
                return;
            }
        }

        this.kind = kind;

        // só guarda lexeme se for necessário
        if (kind == Kind.IDENTIFIER || kind == Kind.INTLITERAL) {
            this.lexeme = lexeme;
        } else {
            this.lexeme = null;
        }
    }

    @Override
    public String toString() {
        if (lexeme != null) {
            return kind + " -> " + lexeme;
        }
        return kind + " -> " + kind.getSpelling();
    }
}