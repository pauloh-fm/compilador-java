package br.edu.compiladorjava.lexer;

public class LexerException extends RuntimeException {

    private final int line;
    private final int column;

    public LexerException(String message, int line, int column) {
        super("Erro léxico (" + line + ", " + column + "): " + message);
        this.line = line;
        this.column = column;
    }
}