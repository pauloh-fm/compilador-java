package br.edu.compiladorjava.lexer;

import java.util.HashMap;
import java.util.Map;

public enum Kind {

    // especiais
    IDENTIFIER(null), // 0
    INTLITERAL(null), // 1

    // operadores inteiros
    PLUS("+"),        // 2
    MINUS("-"),       // 3
    TIMES("*"),       // 4
    DIVIDE("/"),      // 5

    // operadores lógicos
    LT("<"),          // 6
    GT(">"),          // 7
    EQ("="),          // 8

    // palavras-chave
    PROGRAM("program"), // 9
    VAR("var"),         // 10
    INTEGER("integer"), // 11
    BOOLEAN("boolean"), // 12
    BEGIN("begin"),     // 13
    END("end"),         // 14
    IF("if"),           // 15
    THEN("then"),       // 16
    ELSE("else"),       // 17
    WHILE("while"),     // 18
    DO("do"),           // 19
    TRUE("true"),       // 20
    FALSE("false"),     // 21
    OR("or"),           // 22
    AND("and"),         // 23

    // símbolos
    SEMICOLON(";"),     // 24
    COLON(":"),         // 25
    BECOMES(":="),      // 26
    DOT("."),           // 27
    COMMA(","),         // 28
    LPAREN("("),        // 29
    RPAREN(")"),        // 30

    EOT(null);          // 31

    private final String spelling;

    Kind(String spelling) {
        this.spelling = spelling;
    }

    public String getSpelling() {
        return spelling;
    }

    private static final Map<String, Kind> keywords = new HashMap<>();

    static {
        for (Kind k : values()) {
            if (k.spelling != null && Character.isLetter(k.spelling.charAt(0))) {
                keywords.put(k.spelling, k);
            }
        }
    }

    public static Kind fromString(String text) {
        return keywords.get(text);
    }
}