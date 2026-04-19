package br.edu.compiladorjava.lexer;

import java.util.Objects;

public class Token {

    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final int line;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public TokenType type() {
        return type;
    }

    public String lexeme() {
        return lexeme;
    }

    public Object literal() {
        return literal;
    }

    public int line() {
        return line;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Token token)) {
            return false;
        }
        return line == token.line
                && type == token.type
                && Objects.equals(lexeme, token.lexeme)
                && Objects.equals(literal, token.literal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, lexeme, literal, line);
    }

    @Override
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}