package br.edu.compiladorjava.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Lexer {

    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("and", TokenType.AND);
        KEYWORDS.put("begin", TokenType.BEGIN);
        KEYWORDS.put("boolean", TokenType.BOOLEAN);
        KEYWORDS.put("class", TokenType.CLASS);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("end", TokenType.END);
        KEYWORDS.put("false", TokenType.FALSE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("fun", TokenType.FUN);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("integer", TokenType.INTEGER);
        KEYWORDS.put("nil", TokenType.NIL);
        KEYWORDS.put("or", TokenType.OR);
        KEYWORDS.put("print", TokenType.PRINT);
        KEYWORDS.put("program", TokenType.PROGRAM);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("then", TokenType.THEN);
        KEYWORDS.put("true", TokenType.TRUE);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("while", TokenType.WHILE);
    }

    private final String source;
    private final Consumer<String> trace;
    private final List<Token> tokens = new ArrayList<>();
    private int start;
    private int current;
    private int line = 1;

    public Lexer(String source) {
        this(source, null);
    }

    public Lexer(String source, Consumer<String> trace) {
        this.source = source == null ? "" : source;
        this.trace = trace;
    }

    public List<Token> scanTokens() {
        trace("Iniciando analise lexica");
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        Token eof = new Token(TokenType.EOF, "", null, line);
        tokens.add(eof);
        trace("Token: " + eof);
        trace("Fim da analise lexica");
        return List.copyOf(tokens);
    }

    private void scanToken() {
        char c = advance();
        trace("Lendo caractere '" + printable(c) + "' na linha " + line);
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case ':':
                addToken(match('=') ? TokenType.ASSIGN : TokenType.COLON);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '/':
                addToken(TokenType.SLASH);
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw new LexerException("Caractere inesperado na linha " + line + ": " + c);
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        String text = source.substring(start, current);
        TokenType type = KEYWORDS.get(text);
        addToken(type == null ? TokenType.IDENTIFIER : type);
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) {
                advance();
            }
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
            }
            advance();
        }

        if (isAtEnd()) {
            throw new LexerException("String não terminada na linha " + line);
        }

        advance();
        addToken(TokenType.STRING, source.substring(start + 1, current - 1));
    }

    private boolean match(char expected) {
        if (isAtEnd() || source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        Token token = new Token(type, source.substring(start, current), literal, line);
        tokens.add(token);
        trace("Token: " + token);
    }

    private void trace(String message) {
        if (trace != null) {
            trace.accept(message);
        }
    }

    private static String printable(char c) {
        return switch (c) {
            case '\n' -> "\\n";
            case '\r' -> "\\r";
            case '\t' -> "\\t";
            default -> String.valueOf(c);
        };
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private static boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}