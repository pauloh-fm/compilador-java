package br.edu.compiladorjava.lexer;

import java.io.BufferedReader;
import java.io.IOException;

public class Scanner {

    private char currentChar;
    private StringBuffer currentSpelling;
    private BufferedReader reader;

    private int line = 1;
    private int column = 0;

    public Scanner(BufferedReader reader) throws IOException {
        this.reader = reader;
        nextChar();
    }

    private void nextChar() {
        try {
            int c = reader.read();
            if (c == -1) {
                currentChar = '\000';
                return;
            }
            currentChar = (char) c;
            if (currentChar == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        } catch (IOException e) {
            currentChar = '\000';
        }
    }

    private void lexicalError(String msg) {
        throw new LexerException(msg, line, column);
    }

    private void takeIt() {
        currentSpelling.append(currentChar);
        nextChar();
    }

    private void skipIt() {
        nextChar();
    }

    private boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private void scanSeparator() {
        while (currentChar == ' ' || currentChar == '\n' || currentChar == '\r' || currentChar == '\t' || currentChar == '!') {
            if (currentChar == '!') {
                // Pula comentário até o fim da linha
                while (currentChar != '\n' && currentChar != '\000') {
                    skipIt();
                }
            } else {
                skipIt();
            }
        }
    }

    private Token scanToken() {
        // IDENTIFICADOR ou PALAVRA-RESERVADA
        if (isLetter(currentChar)) {
            takeIt();
            while (isLetter(currentChar) || isDigit(currentChar))
                takeIt();
            return new Token(Kind.IDENTIFIER, currentSpelling.toString());
        }

        // NÚMERO
        if (isDigit(currentChar)) {
            takeIt();
            while (isDigit(currentChar))
                takeIt();
            return new Token(Kind.INTLITERAL, currentSpelling.toString());
        }

        switch (currentChar) {
            case '+': takeIt(); return new Token(Kind.PLUS, null);
            case '-': takeIt(); return new Token(Kind.MINUS, null);
            case '*': takeIt(); return new Token(Kind.TIMES, null);
            case '/': takeIt(); return new Token(Kind.DIVIDE, null);
            case '<': takeIt(); return new Token(Kind.LT, null);
            case '>': takeIt(); return new Token(Kind.GT, null);
            case '=': takeIt(); return new Token(Kind.EQ, null);
            case ';': takeIt(); return new Token(Kind.SEMICOLON, null);
            case '.': takeIt(); return new Token(Kind.DOT, null);
            case ',': takeIt(); return new Token(Kind.COMMA, null);
            case '(': takeIt(); return new Token(Kind.LPAREN, null);
            case ')': takeIt(); return new Token(Kind.RPAREN, null);

            case ':':
                takeIt();
                if (currentChar == '=') {
                    takeIt();
                    return new Token(Kind.BECOMES, null);
                }
                return new Token(Kind.COLON, null);

            case '\000':
                return new Token(Kind.EOT, null);

            default:
                lexicalError("caractere inválido: " + currentChar);
                return null;
        }
    }

    public Token scan() {
        currentSpelling = new StringBuffer();
        scanSeparator(); // Pula espaços e comentários antes de buscar o token
        return scanToken();
    }
}