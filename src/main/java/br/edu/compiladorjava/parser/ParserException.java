package br.edu.compiladorjava.parser;

/**
 * Exceção lançada quando ocorre um erro durante a análise sintática.
 */
public class ParserException extends RuntimeException {

    private final int line;
    private final int column;

    /**
     * Cria uma exceção sintática com mensagem.
     */
    public ParserException(String message) {
        super(message);
        this.line = -1;
        this.column = -1;
    }

    /**
     * Cria uma exceção sintática com posição no código.
     */
    public ParserException(String message, int line, int column) {
        super(message);
        this.line = line;
        this.column = column;
    }

    /**
     * Cria uma exceção sintática com causa.
     */
    public ParserException(String message, Throwable cause) {
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
