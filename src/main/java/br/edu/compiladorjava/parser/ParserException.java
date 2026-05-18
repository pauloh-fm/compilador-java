package br.edu.compiladorjava.parser;

public class ParserException extends RuntimeException {

    public ParserException(String message) {
        super(message);
    }

    public ParserException(int line, int column, String message) {
        super("Erro sintatico na linha " + line + ", coluna " + column + ": " + message);
    }
}
