package br.edu.compiladorjava.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class LexerTest {

    @Test
    void shouldScanBasicTokens() {
        Lexer lexer = new Lexer("print 123 + foo");

        List<Token> tokens = lexer.scanTokens();

        assertEquals(List.of(
                new Token(TokenType.PRINT, "print", null, 1),
                new Token(TokenType.NUMBER, "123", 123.0, 1),
                new Token(TokenType.PLUS, "+", null, 1),
                new Token(TokenType.IDENTIFIER, "foo", null, 1),
                new Token(TokenType.EOF, "", null, 1)), tokens);
    }
}