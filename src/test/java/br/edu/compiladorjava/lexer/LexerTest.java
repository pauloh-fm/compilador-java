package br.edu.compiladorjava.lexer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LexerTest {

    @Test
    void tokenizeSimpleProgramShouldProduceExpectedTokens() {
        String source = "program teste; begin x := 1 + 2; end.";

        List<TokenType> types = new Lexer(source).tokenize().stream()
                .map(Token::type)
                .toList();

        assertEquals(List.of(
                TokenType.PROGRAM,
                TokenType.IDENTIFIER,
                TokenType.SEMICOLON,
                TokenType.BEGIN,
                TokenType.IDENTIFIER,
                TokenType.ASSIGN,
                TokenType.INTEGER_LITERAL,
                TokenType.PLUS,
                TokenType.INTEGER_LITERAL,
                TokenType.SEMICOLON,
                TokenType.END,
                TokenType.DOT,
                TokenType.EOF
        ), types);
    }

    @Test
    void tokenizeShouldIgnoreCommentsAndRecognizeFloats() {
        String source = "program teste; /* comentario */ begin x := .5; y := 2.; end.";

        List<TokenType> types = new Lexer(source).tokenize().stream()
                .map(Token::type)
                .toList();

        assertEquals(List.of(
                TokenType.PROGRAM,
                TokenType.IDENTIFIER,
                TokenType.SEMICOLON,
                TokenType.BEGIN,
                TokenType.IDENTIFIER,
                TokenType.ASSIGN,
                TokenType.FLOAT_LITERAL,
                TokenType.SEMICOLON,
                TokenType.IDENTIFIER,
                TokenType.ASSIGN,
                TokenType.FLOAT_LITERAL,
                TokenType.SEMICOLON,
                TokenType.END,
                TokenType.DOT,
                TokenType.EOF
        ), types);
    }

    @Test
    void tokenizeShouldFailOnInvalidCharacter() {
        assertThrows(LexerException.class, () -> new Lexer("program teste; @").tokenize());
    }
}