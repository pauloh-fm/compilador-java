package br.edu.compiladorjava.parser;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import br.edu.compiladorjava.lexer.Lexer;

/**
 * Testes para o parser recursivo descendente.
 */
@DisplayName("Parser Recursivo Descendente")
public class ParserTest {

    @Test
    @DisplayName("Deve aceitar programa simples com atribuição")
    public void testSimpleProgram() {
        String code = "program test;\n" +
                "begin\n" +
                "  x := 5;\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Programa simples deve ser aceito");
    }

    @Test
    @DisplayName("Deve aceitar declaração de variável")
    public void testVariableDeclaration() {
        String code = "program test;\n" +
                "var x : integer;\n" +
                "begin\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Declaração de variável deve ser aceita");
    }

    @Test
    @DisplayName("Deve aceitar atribuição simples")
    public void testSimpleAssignment() {
        String code = "program test;\n" +
                "begin\n" +
                "  x := 42;\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Atribuição simples deve ser aceita");
    }

    @Test
    @DisplayName("Deve aceitar expressão com operadores")
    public void testExpressionWithOperators() {
        String code = "program test;\n" +
                "begin\n" +
                "  x := 2 + 3 * 4;\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Expressão com operadores deve ser aceita");
    }

    @Test
    @DisplayName("Deve aceitar condicional if-then-else")
    public void testIfThenElse() {
        String code = "program test;\n" +
                "begin\n" +
                "  if x > 0 then y := 1 else y := -1;\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Condicional if-then-else deve ser aceita");
    }

    @Test
    @DisplayName("Deve aceitar loop while")
    public void testWhileLoop() {
        String code = "program test;\n" +
                "begin\n" +
                "  while x > 0 do x := x - 1;\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Loop while deve ser aceito");
    }

    @Test
    @DisplayName("Deve aceitar múltiplas declarações")
    public void testMultipleDeclarations() {
        String code = "program test;\n" +
                "var x : integer;\n" +
                "var y : boolean;\n" +
                "begin\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Múltiplas declarações devem ser aceitas");
    }

    @Test
    @DisplayName("Deve aceitar múltiplos comandos")
    public void testMultipleCommands() {
        String code = "program test;\n" +
                "begin\n" +
                "  x := 1;\n" +
                "  y := 2;\n" +
                "  z := 3;\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Múltiplos comandos devem ser aceitos");
    }

    @Test
    @DisplayName("Deve aceitar expressão booleana")
    public void testBooleanExpression() {
        String code = "program test;\n" +
                "begin\n" +
                "  x := true;\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Expressão booleana deve ser aceita");
    }

    @Test
    @DisplayName("Deve aceitar comando-composto aninhado")
    public void testNestedCommandCompound() {
        String code = "program test;\n" +
                "begin\n" +
                "  if x > 0 then\n" +
                "    begin\n" +
                "      y := 1;\n" +
                "    end;\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Comando-composto aninhado deve ser aceito");
    }

    @Test
    @DisplayName("Deve lançar exceção para token não esperado")
    public void testUnexpectedToken() {
        String code = "program test\n"; // Falta ;

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertThrows(ParserException.class, () -> parser.parseProgram(),
                "Deve lançar ParserException para token não esperado");
    }

    @Test
    @DisplayName("Deve lançar exceção para falta de begin")
    public void testMissingBegin() {
        String code = "program test;\n" +
                "end\n"; // Falta begin

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertThrows(ParserException.class, () -> parser.parseProgram(),
                "Deve lançar ParserException quando begin está faltando");
    }

    @Test
    @DisplayName("Deve lançar exceção para falta de end")
    public void testMissingEnd() {
        String code = "program test;\n" +
                "begin\n"; // Falta end

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertThrows(ParserException.class, () -> parser.parseProgram(),
                "Deve lançar ParserException quando end está faltando");
    }

    @Test
    @DisplayName("Deve aceitar programa completo")
    public void testCompleteProgram() {
        String code = "program fatorial;\n" +
                "var n : integer;\n" +
                "var result : integer;\n" +
                "begin\n" +
                "  n := 5;\n" +
                "  result := 1;\n" +
                "  while n > 1 do\n" +
                "    begin\n" +
                "      result := result * n;\n" +
                "      n := n - 1;\n" +
                "    end;\n" +
                "end\n";

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        assertDoesNotThrow(() -> parser.parseProgram(),
                "Programa completo deve ser aceito");
    }
}
