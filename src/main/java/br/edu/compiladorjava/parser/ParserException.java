package br.edu.compiladorjava.parser;

import br.edu.compiladorjava.lexer.Token;

public class ParserException extends RuntimeException {

    public ParserException(String message, Token token) {
        super(String.format("Erro sintático na linha %d, coluna %d (Token: %s) - %s",
                token.line, token.column, token.lexeme, message));
    }
}