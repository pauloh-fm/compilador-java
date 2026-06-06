package br.edu.compiladorjava.semantic;

public class SemanticException extends RuntimeException {

    public SemanticException(String message, int line, int column) {
        super(String.format("Erro semântico na linha %d, coluna %d - %s", line, column, message));
    }
}