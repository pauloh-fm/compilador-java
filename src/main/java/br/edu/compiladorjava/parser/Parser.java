package br.edu.compiladorjava.parser;

import br.edu.compiladorjava.lexer.Lexer;
import br.edu.compiladorjava.lexer.Token;
import br.edu.compiladorjava.lexer.TokenType;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current;

    public Parser(String source) {
        this(new Lexer(source).tokenize());
    }

    public Parser(List<Token> tokens) {
        this.tokens = List.copyOf(tokens);
    }

    public void parse() {
        parseProgram();
        consume(TokenType.EOF, "esperado fim do arquivo");
    }

    private void parseProgram() {
        consume(TokenType.PROGRAM, "esperado 'program'");
        consume(TokenType.IDENTIFIER, "esperado o nome do programa");
        consume(TokenType.SEMICOLON, "esperado ';' apos o cabecalho do programa");
        parseDeclarations();
        parseCompoundStatement();
        consume(TokenType.DOT, "esperado '.' ao final do programa");
    }

    private void parseDeclarations() {
        while (match(TokenType.VAR)) {
            parseDeclaration();
            consume(TokenType.SEMICOLON, "esperado ';' apos a declaracao de variavel");
        }
    }

    private void parseDeclaration() {
        parseIdentifierList();
        consume(TokenType.COLON, "esperado ':' na declaracao de variavel");
        parseType();
    }

    private void parseIdentifierList() {
        consume(TokenType.IDENTIFIER, "esperado identificador");
        while (match(TokenType.COMMA)) {
            consume(TokenType.IDENTIFIER, "esperado identificador apos ','");
        }
    }

    private void parseType() {
        if (match(TokenType.INTEGER) || match(TokenType.BOOLEAN)) {
            return;
        }

        throw error(peek(), "esperado um tipo ('integer' ou 'boolean')");
    }

    private void parseCompoundStatement() {
        consume(TokenType.BEGIN, "esperado 'begin'");
        parseStatementList();
        consume(TokenType.END, "esperado 'end'");
    }

    private void parseStatementList() {
        if (check(TokenType.END)) {
            return;
        }

        while (!check(TokenType.END) && !check(TokenType.EOF)) {
            parseStatement();
            if (match(TokenType.SEMICOLON)) {
                if (check(TokenType.END)) {
                    return;
                }
                continue;
            }

            if (!check(TokenType.END)) {
                throw error(peek(), "esperado ';' ou 'end'");
            }
        }
    }

    private void parseStatement() {
        if (check(TokenType.IDENTIFIER)) {
            parseAssignment();
            return;
        }

        if (check(TokenType.IF)) {
            parseConditional();
            return;
        }

        if (check(TokenType.WHILE)) {
            parseIteration();
            return;
        }

        if (check(TokenType.BEGIN)) {
            parseCompoundStatement();
            return;
        }

        throw error(peek(), "esperado um comando");
    }

    private void parseAssignment() {
        consume(TokenType.IDENTIFIER, "esperado uma variavel");
        consume(TokenType.ASSIGN, "esperado ':=' na atribuicao");
        parseExpression();
    }

    private void parseConditional() {
        consume(TokenType.IF, "esperado 'if'");
        parseExpression();
        consume(TokenType.THEN, "esperado 'then'");
        parseStatement();
        if (match(TokenType.ELSE)) {
            parseStatement();
        }
    }

    private void parseIteration() {
        consume(TokenType.WHILE, "esperado 'while'");
        parseExpression();
        consume(TokenType.DO, "esperado 'do'");
        parseStatement();
    }

    private void parseExpression() {
        parseSimpleExpression();
        if (match(TokenType.LESS) || match(TokenType.GREATER) || match(TokenType.EQUAL)) {
            parseSimpleExpression();
        }
    }

    private void parseSimpleExpression() {
        parseTerm();
        while (match(TokenType.PLUS) || match(TokenType.MINUS) || match(TokenType.OR)) {
            parseTerm();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (match(TokenType.STAR) || match(TokenType.SLASH) || match(TokenType.AND)) {
            parseFactor();
        }
    }

    private void parseFactor() {
        if (match(TokenType.IDENTIFIER)) {
            return;
        }

        if (match(TokenType.TRUE) || match(TokenType.FALSE)
                || match(TokenType.INTEGER_LITERAL) || match(TokenType.FLOAT_LITERAL)) {
            return;
        }

        if (match(TokenType.LPAREN)) {
            parseExpression();
            consume(TokenType.RPAREN, "esperado ')'");
            return;
        }

        throw error(peek(), "esperado uma expressao valida");
    }

    private boolean match(TokenType expected) {
        if (!check(expected)) {
            return false;
        }
        advance();
        return true;
    }

    private void consume(TokenType expected, String message) {
        if (check(expected)) {
            advance();
            return;
        }

        throw error(peek(), message + " (encontrado '" + peek().lexeme() + "')");
    }

    private boolean check(TokenType expected) {
        return !isAtEnd() && peek().type() == expected;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParserException error(Token token, String message) {
        return new ParserException(token.line(), token.column(), message);
    }
}
