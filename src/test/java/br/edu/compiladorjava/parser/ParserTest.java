package br.edu.compiladorjava.parser;

import br.edu.compiladorjava.lexer.Lexer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    @Test
    void parseValidProgramShouldSucceed() {
        String source = """
                program teste;
                var x: integer;
                var flag: boolean;
                begin
                  x := 1;
                  if true then
                    flag := false
                  else
                    flag := true;
                  while x < 10 do
                    x := x + 1
                end.
                """;

        assertDoesNotThrow(() -> new Parser(new Lexer(source).tokenize()).parse());
    }

    @Test
    void parseInvalidProgramShouldFail() {
        String source = "program teste; begin x := 1 end.";

        assertThrows(ParserException.class, () -> new Parser(new Lexer(source).tokenize()).parse());
    }

    @Test
    void parseComplexProgramWithArithmeticConditionalAndLoopShouldSucceed() {
        String source = """
                program exemplo;
                var x : integer;
                var y : integer;
                var flag : boolean;
                begin
                    x := 10;
                    y := 5;
                    x := x + y * 2;
                    if x > y then
                        flag := true
                    else
                        flag := false;
                    while x > 0 do
                    begin
                        x := x - 1;
                    end;
                end.
                """;

        assertDoesNotThrow(() -> new Parser(new Lexer(source).tokenize()).parse());
    }
}