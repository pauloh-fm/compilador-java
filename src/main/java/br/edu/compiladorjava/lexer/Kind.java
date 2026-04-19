package br.edu.compiladorjava.lexer;

import java.util.HashMap;
import java.util.Map;

public enum Kind {

    // especiais
    IDENTIFIER(null),
    INTLITERAL(null),

    // operadores
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),

    LT("<"),
    GT(">"),
    EQ("="),

    // palavras-chave
    PROGRAM("program"),
    VAR("var"),
    INTEGER("integer"),
    BOOLEAN("boolean"),
    BEGIN("begin"),
    END("end"),
    IF("if"),
    THEN("then"),
    ELSE("else"),
    WHILE("while"),
    DO("do"),
    TRUE("true"),
    FALSE("false"),
    OR("or"),
    AND("and"),

    // símbolos
    SEMICOLON(";"),
    COLON(":"),
    BECOMES(":="),
    DOT("."),
    COMMA(","),
    LPAREN("("),
    RPAREN(")"),

    EOT(null);

    private final String spelling;

    Kind(String spelling) {
        this.spelling = spelling;
    }

    public String getSpelling() {
        return spelling;
    }

    // 🔥 mapa para lookup rápido (sem switch gigante)
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