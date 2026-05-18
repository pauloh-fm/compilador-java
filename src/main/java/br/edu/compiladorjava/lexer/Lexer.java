package br.edu.compiladorjava.lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final String source;
    private final int length;
    private int current;
    private int line = 1;
    private int column = 1;
    private int tokenLine = 1;
    private int tokenColumn = 1;

    public Lexer(String source) {
        this.source = source == null ? "" : source;
        this.length = this.source.length();
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (!isAtEnd()) {
            tokenLine = line;
            tokenColumn = column;
            scanToken(tokens);
        }

        tokens.add(new Token(TokenType.EOF, "", line, column));
        return List.copyOf(tokens);
    }

    private void scanToken(List<Token> tokens) {
        char currentChar = advance();

        switch (currentChar) {
            case ' ':
            case '\r':
            case '\t':
            case '\n':
                return;
            case '+':
                addToken(tokens, TokenType.PLUS, "+");
                return;
            case '-':
                addToken(tokens, TokenType.MINUS, "-");
                return;
            case '*':
                addToken(tokens, TokenType.STAR, "*");
                return;
            case '=':
                addToken(tokens, TokenType.EQUAL, "=");
                return;
            case '<':
                addToken(tokens, TokenType.LESS, "<");
                return;
            case '>':
                addToken(tokens, TokenType.GREATER, ">");
                return;
            case ',':
                addToken(tokens, TokenType.COMMA, ",");
                return;
            case ';':
                addToken(tokens, TokenType.SEMICOLON, ";");
                return;
            case '(': 
                addToken(tokens, TokenType.LPAREN, "(");
                return;
            case ')':
                addToken(tokens, TokenType.RPAREN, ")");
                return;
            case ':':
                if (match('=')) {
                    addToken(tokens, TokenType.ASSIGN, ":=");
                } else {
                    addToken(tokens, TokenType.COLON, ":");
                }
                return;
            case '.':
                if (isDigit(peek())) {
                    scanFloatStartingWithDot(tokens);
                } else {
                    addToken(tokens, TokenType.DOT, ".");
                }
                return;
            case '/':
                if (match('/')) {
                    skipLineComment();
                    return;
                }
                if (match('*')) {
                    skipBlockComment();
                    return;
                }
                addToken(tokens, TokenType.SLASH, "/");
                return;
            case '{':
                skipBraceComment();
                return;
            default:
                if (isDigit(currentChar)) {
                    scanNumber(tokens, currentChar);
                    return;
                }
                if (isIdentifierStart(currentChar)) {
                    scanIdentifier(tokens, currentChar);
                    return;
                }
                throw new LexerException(tokenLine, tokenColumn, "Caractere invalido '" + currentChar + "'.");
        }
    }

    private void scanIdentifier(List<Token> tokens, char firstChar) {
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(firstChar);

        while (!isAtEnd() && isIdentifierPart(peek())) {
            lexeme.append(advance());
        }

        String text = lexeme.toString();
        addToken(tokens, keywordType(text), text);
    }

    private void scanNumber(List<Token> tokens, char firstDigit) {
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(firstDigit);

        while (!isAtEnd() && isDigit(peek())) {
            lexeme.append(advance());
        }

        if (!isAtEnd() && peek() == '.') {
            lexeme.append(advance());
            while (!isAtEnd() && isDigit(peek())) {
                lexeme.append(advance());
            }
            addToken(tokens, TokenType.FLOAT_LITERAL, lexeme.toString());
            return;
        }

        addToken(tokens, TokenType.INTEGER_LITERAL, lexeme.toString());
    }

    private void scanFloatStartingWithDot(List<Token> tokens) {
        StringBuilder lexeme = new StringBuilder();
        lexeme.append('.');

        while (!isAtEnd() && isDigit(peek())) {
            lexeme.append(advance());
        }

        addToken(tokens, TokenType.FLOAT_LITERAL, lexeme.toString());
    }

    private void skipLineComment() {
        while (!isAtEnd() && peek() != '\n') {
            advance();
        }
    }

    private void skipBlockComment() {
        while (!isAtEnd()) {
            if (peek() == '*' && peekNext() == '/') {
                advance();
                advance();
                return;
            }
            advance();
        }

        throw new LexerException(tokenLine, tokenColumn, "Comentario de bloco nao finalizado.");
    }

    private void skipBraceComment() {
        while (!isAtEnd() && peek() != '}') {
            advance();
        }

        if (isAtEnd()) {
            throw new LexerException(tokenLine, tokenColumn, "Comentario nao finalizado.");
        }

        advance();
    }

    private TokenType keywordType(String lexeme) {
        return switch (lexeme) {
            case "program" -> TokenType.PROGRAM;
            case "var" -> TokenType.VAR;
            case "integer" -> TokenType.INTEGER;
            case "boolean" -> TokenType.BOOLEAN;
            case "begin" -> TokenType.BEGIN;
            case "end" -> TokenType.END;
            case "if" -> TokenType.IF;
            case "then" -> TokenType.THEN;
            case "else" -> TokenType.ELSE;
            case "while" -> TokenType.WHILE;
            case "do" -> TokenType.DO;
            case "true" -> TokenType.TRUE;
            case "false" -> TokenType.FALSE;
            case "or" -> TokenType.OR;
            case "and" -> TokenType.AND;
            default -> TokenType.IDENTIFIER;
        };
    }

    private void addToken(List<Token> tokens, TokenType type, String lexeme) {
        tokens.add(new Token(type, lexeme, tokenLine, tokenColumn));
    }

    private char advance() {
        char currentChar = source.charAt(current++);
        if (currentChar == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        return currentChar;
    }

    private boolean match(char expected) {
        if (isAtEnd() || source.charAt(current) != expected) {
            return false;
        }

        advance();
        return true;
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= length) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    private boolean isAtEnd() {
        return current >= length;
    }

    private static boolean isDigit(char character) {
        return character >= '0' && character <= '9';
    }

    private static boolean isIdentifierStart(char character) {
        return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == '_';
    }

    private static boolean isIdentifierPart(char character) {
        return isIdentifierStart(character) || isDigit(character);
    }
}
