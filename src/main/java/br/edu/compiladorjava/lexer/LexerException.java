package br.edu.compiladorjava.lexer;

public class LexerException extends RuntimeException {

    private final int line;
    private final int column;

    /**
     * Cria uma exceção léxica com mensagem.
     */
    public LexerException(String message) {
        super(message);
        this.line = -1;
        this.column = -1;
    }

    /**
     * Cria uma exceção léxica com posição no código.
     */
    public LexerException(String message, int line, int column) {
        super(String.format("%s at line %d, column %d", message, line, column));
        this.line = line;
        this.column = column;
    }

    /**
     * Cria uma exceção léxica com causa.
     */
    public LexerException(String message, Throwable cause) {
        super(message, cause);
        this.line = -1;
        this.column = -1;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
