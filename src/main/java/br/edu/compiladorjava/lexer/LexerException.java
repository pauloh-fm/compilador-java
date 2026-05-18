package br.edu.compiladorjava.lexer;

public class LexerException extends RuntimeException {

    public LexerException(String message) {
        super(message);
    }

    public LexerException(int line, int column, String message) {
        super("Erro lexico na linha " + line + ", coluna " + column + ": " + message);
    }
}
