package br.edu.compiladorjava.lexer;

public record Token(TokenType type, String lexeme, int line, int column) {

    public Token {
        if (type == null) {
            throw new IllegalArgumentException("O tipo do token nao pode ser nulo.");
        }
        if (lexeme == null) {
            throw new IllegalArgumentException("O lexema do token nao pode ser nulo.");
        }
    }

    @Override
    public String toString() {
        return type + "('" + lexeme + "')@" + line + ":" + column;
    }
}
