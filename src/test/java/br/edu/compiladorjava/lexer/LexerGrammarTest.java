package br.edu.compiladorjava.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class LexerGrammarTest {

    @Test
    void shouldScanLanguageGrammarTokens() {
        Lexer lexer = new Lexer("program teste; var x: integer; begin x := 10; end.");

        List<Token> tokens = lexer.scanTokens();

        assertEquals(List.of(
                new Token(TokenType.PROGRAM, "program", null, 1),
                new Token(TokenType.IDENTIFIER, "teste", null, 1),
                new Token(TokenType.SEMICOLON, ";", null, 1),
                new Token(TokenType.VAR, "var", null, 1),
                new Token(TokenType.IDENTIFIER, "x", null, 1),
                new Token(TokenType.COLON, ":", null, 1),
                new Token(TokenType.INTEGER, "integer", null, 1),
                new Token(TokenType.SEMICOLON, ";", null, 1),
                new Token(TokenType.BEGIN, "begin", null, 1),
                new Token(TokenType.IDENTIFIER, "x", null, 1),
                new Token(TokenType.ASSIGN, ":=", null, 1),
                new Token(TokenType.NUMBER, "10", 10.0, 1),
                new Token(TokenType.SEMICOLON, ";", null, 1),
                new Token(TokenType.END, "end", null, 1),
                new Token(TokenType.DOT, ".", null, 1),
                new Token(TokenType.EOF, "", null, 1)), tokens);
    }

    @Test
    void shouldScanRelationalAdditiveAndMultiplicativeOperators() {
        Lexer lexer = new Lexer("if a < b then a := a + 1 - 2 * 3 / 4 and true or false = b do begin end");

        List<Token> tokens = lexer.scanTokens();

        assertEquals(List.of(
                new Token(TokenType.IF, "if", null, 1),
                new Token(TokenType.IDENTIFIER, "a", null, 1),
                new Token(TokenType.LESS, "<", null, 1),
                new Token(TokenType.IDENTIFIER, "b", null, 1),
                new Token(TokenType.THEN, "then", null, 1),
                new Token(TokenType.IDENTIFIER, "a", null, 1),
                new Token(TokenType.ASSIGN, ":=", null, 1),
                new Token(TokenType.IDENTIFIER, "a", null, 1),
                new Token(TokenType.PLUS, "+", null, 1),
                new Token(TokenType.NUMBER, "1", 1.0, 1),
                new Token(TokenType.MINUS, "-", null, 1),
                new Token(TokenType.NUMBER, "2", 2.0, 1),
                new Token(TokenType.STAR, "*", null, 1),
                new Token(TokenType.NUMBER, "3", 3.0, 1),
                new Token(TokenType.SLASH, "/", null, 1),
                new Token(TokenType.NUMBER, "4", 4.0, 1),
                new Token(TokenType.AND, "and", null, 1),
                new Token(TokenType.TRUE, "true", null, 1),
                new Token(TokenType.OR, "or", null, 1),
                new Token(TokenType.FALSE, "false", null, 1),
                new Token(TokenType.EQUAL, "=", null, 1),
                new Token(TokenType.IDENTIFIER, "b", null, 1),
                new Token(TokenType.DO, "do", null, 1),
                new Token(TokenType.BEGIN, "begin", null, 1),
                new Token(TokenType.END, "end", null, 1),
                new Token(TokenType.EOF, "", null, 1)
        ), tokens);
    }
}