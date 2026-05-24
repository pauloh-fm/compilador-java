package br.edu.compiladorjava.parser;

import br.edu.compiladorjava.lexer.Kind;
import br.edu.compiladorjava.lexer.Scanner;
import br.edu.compiladorjava.lexer.Token;

public class Parser {

    private final Scanner scanner;
    private Token currentToken;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.currentToken = scanner.scan(); // Inicializa o lookahead
    }

    private void accept(Kind expectedKind) {
        if (currentToken.kind == expectedKind) {
            currentToken = scanner.scan();
        } else {
            throw new ParserException(
                    "Esperava " + expectedKind.name() + " mas encontrou " + currentToken.kind.name(),
                    currentToken
            );
        }
    }

    private void acceptIt() {
        currentToken = scanner.scan();
    }

    // <programa> ::= program <id> ; <corpo> .
    public void parse() {
        parsePrograma();
        if (currentToken.kind != Kind.EOT) {
            throw new ParserException("Tokens inesperados no final do arquivo.", currentToken);
        }
    }

    private void parsePrograma() {
        accept(Kind.PROGRAM);
        accept(Kind.IDENTIFIER);
        accept(Kind.SEMICOLON);
        parseCorpo();
        accept(Kind.DOT);
    }

    // <corpo> ::= <declarações> <comando-composto>
    private void parseCorpo() {
        parseDeclaracoes();
        parseComandoComposto();
    }

    // <declarações> ::= <declaração> ; <declarações> | <vazio>
    private void parseDeclaracoes() {
        // FIRST(<declaração>) = { var }
        while (currentToken.kind == Kind.VAR) {
            parseDeclaracao();
            accept(Kind.SEMICOLON);
        }
    }

    // <declaração> ::= var <id> : <tipo>
    private void parseDeclaracao() {
        accept(Kind.VAR);
        accept(Kind.IDENTIFIER);
        accept(Kind.COLON);
        parseTipo();
    }

    // <tipo> ::= integer | boolean
    private void parseTipo() {
        if (currentToken.kind == Kind.INTEGER || currentToken.kind == Kind.BOOLEAN) {
            acceptIt();
        } else {
            throw new ParserException("Tipo esperado (integer ou boolean)", currentToken);
        }
    }

    // <comando-composto> ::= begin <lista-de-comandos> end
    private void parseComandoComposto() {
        accept(Kind.BEGIN);
        parseListaDeComandos();
        accept(Kind.END);
    }

    // <lista-de-comandos> ::= <comando> ; <lista-de-comandos> | <vazio>
    private void parseListaDeComandos() {
        while (isComandoStart()) {
            parseComando();
            accept(Kind.SEMICOLON);
        }
    }

    private boolean isComandoStart() {
        return currentToken.kind == Kind.IDENTIFIER ||
                currentToken.kind == Kind.IF ||
                currentToken.kind == Kind.WHILE ||
                currentToken.kind == Kind.BEGIN;
    }

    // <comando> ::= <atribuição> | <condicional> | <iterativo> | <comando-composto>
    private void parseComando() {
        switch (currentToken.kind) {
            case IDENTIFIER:
                parseAtribuicao();
                break;
            case IF:
                parseCondicional();
                break;
            case WHILE:
                parseIterativo();
                break;
            case BEGIN:
                parseComandoComposto();
                break;
            default:
                throw new ParserException("Comando inválido esperado", currentToken);
        }
    }

    // <atribuição> ::= <variável> := <expressão>
    private void parseAtribuicao() {
        accept(Kind.IDENTIFIER); // <variável>
        accept(Kind.BECOMES);    // :=
        parseExpressao();
    }

    // <condicional> ::= if <expressão> then <comando> ( else <comando> | <vazio> )
    private void parseCondicional() {
        accept(Kind.IF);
        parseExpressao();
        accept(Kind.THEN);
        parseComando();
        if (currentToken.kind == Kind.ELSE) {
            acceptIt();
            parseComando();
        }
    }

    // <iterativo> ::= while <expressão> do <comando>
    private void parseIterativo() {
        accept(Kind.WHILE);
        parseExpressao();
        accept(Kind.DO);
        parseComando();
    }

    // <expressão> ::= <expressão-simples> ( <op-rel> <expressão-simples> | <vazio> )
    private void parseExpressao() {
        parseExpressaoSimples();
        if (isOpRel()) {
            acceptIt(); // consome <, >, =
            parseExpressaoSimples();
        }
    }

    private boolean isOpRel() {
        return currentToken.kind == Kind.LT ||
                currentToken.kind == Kind.GT ||
                currentToken.kind == Kind.EQ;
    }

    // <expressão-simples> ::= <termo> ( <op-ad> <termo> )*
    private void parseExpressaoSimples() {
        parseTermo();
        while (isOpAd()) {
            acceptIt(); // consome +, -, or
            parseTermo();
        }
    }

    private boolean isOpAd() {
        return currentToken.kind == Kind.PLUS ||
                currentToken.kind == Kind.MINUS ||
                currentToken.kind == Kind.OR;
    }

    // <termo> ::= <fator> ( <op-mul> <fator> )*
    private void parseTermo() {
        parseFator();
        while (isOpMul()) {
            acceptIt(); // consome *, /, and
            parseFator();
        }
    }

    private boolean isOpMul() {
        return currentToken.kind == Kind.TIMES ||
                currentToken.kind == Kind.DIVIDE ||
                currentToken.kind == Kind.AND;
    }

    // <fator> ::= <variável> | <literal> | "(" <expressão> ")"
    private void parseFator() {
        if (currentToken.kind == Kind.IDENTIFIER) {
            acceptIt();
        } else if (currentToken.kind == Kind.INTLITERAL) {
            acceptIt();
        } else if (currentToken.kind == Kind.TRUE || currentToken.kind == Kind.FALSE) {
            acceptIt();
        } else if (currentToken.kind == Kind.LPAREN) {
            acceptIt();
            parseExpressao();
            accept(Kind.RPAREN);
        } else {
            throw new ParserException("Fator inválido. Era esperado um Identificador, Número, Booleano ou Expressão entre parênteses.", currentToken);
        }
    }
}