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
}