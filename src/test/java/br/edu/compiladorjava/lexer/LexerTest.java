package br.edu.compiladorjava.lexer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Testes para o analisador léxico (Lexer).
 * Cobre todos os tipos de token definidos na gramática léxica.
 */
@DisplayName("Analisador Léxico (Lexer)")
public class LexerTest {

    @Test
    @DisplayName("Deve reconhecer identificadores simples")
    public void testSimpleIdentifier() {
        Lexer lexer = new Lexer("variable");
        Token token = lexer.scan();
        assertEquals(TokenType.IDENTIFIER, token.getKind());
        assertEquals("variable", token.getSpelling());
    }

    @Test
    @DisplayName("Deve reconhecer literais inteiros")
    public void testIntegerLiteral() {
        Lexer lexer = new Lexer("42");
        Token token = lexer.scan();
        assertEquals(TokenType.INTLITERAL, token.getKind());
        assertEquals("42", token.getSpelling());
    }

    @Test
    @DisplayName("Deve reconhecer palavra-chave 'begin'")
    public void testKeywordBegin() {
        Lexer lexer = new Lexer("begin");
        Token token = lexer.scan();
        assertEquals(TokenType.BEGIN, token.getKind());
    }

    @Test
    @DisplayName("Deve reconhecer operador '+'")
    public void testPlusOperator() {
        Lexer lexer = new Lexer("+");
        Token token = lexer.scan();
        assertEquals(TokenType.PLUS, token.getKind());
    }

    @Test
    @DisplayName("Deve reconhecer ':=' (BECOMES)")
    public void testBecomesOperator() {
        Lexer lexer = new Lexer(":=");
        Token token = lexer.scan();
        assertEquals(TokenType.BECOMES, token.getKind());
    }

    @Test
    @DisplayName("Deve reconhecer EOT")
    public void testEndOfText() {
        Lexer lexer = new Lexer("");
        Token token = lexer.scan();
        assertEquals(TokenType.EOT, token.getKind());
    }

    @Test
    @DisplayName("Deve pular espaços em branco")
    public void testSkipWhitespace() {
        Lexer lexer = new Lexer("  variable  ");
        Token token = lexer.scan();
        assertEquals(TokenType.IDENTIFIER, token.getKind());
        assertEquals("variable", token.getSpelling());
    }

    @Test
    @DisplayName("Deve processar sequência de tokens")
    public void testTokenSequence() {
        Lexer lexer = new Lexer("let x := 10");

        Token token1 = lexer.scan();
        assertEquals(TokenType.LET, token1.getKind());

        Token token2 = lexer.scan();
        assertEquals(TokenType.IDENTIFIER, token2.getKind());

        Token token3 = lexer.scan();
        assertEquals(TokenType.BECOMES, token3.getKind());

        Token token4 = lexer.scan();
        assertEquals(TokenType.INTLITERAL, token4.getKind());
    }
}
