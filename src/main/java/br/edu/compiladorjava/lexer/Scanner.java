package br.edu.compiladorjava.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private char currentChar;
    private final BufferedReader reader;
    private StringBuilder currentSpelling;

    // posição atual no arquivo
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
        }
        catch (IOException e) {
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

    // ignora espaços/comentários
    private void scanSeparator() {
        while (true) {
            // espaços
            while (currentChar == ' ' || currentChar == '\n' ||
                    currentChar == '\r' || currentChar == '\t') {
                skipIt();
            }
            // comentário
            if (currentChar == '!') {
                while (currentChar != '\n' && currentChar != '\000') {
                    skipIt();
                }
            } else {
                break;
            }
        }
    }

    private Kind scanToken() {

        // IDENTIFICADOR
        if (isLetter(currentChar)) {
            takeIt();
            while (isLetter(currentChar) || isDigit(currentChar)) {
                takeIt();
            }
            return Kind.IDENTIFIER;
        }

        // NÚMERO
        if (isDigit(currentChar)) {
            takeIt();
            while (isDigit(currentChar)) {
                takeIt();
            }
            return Kind.INTLITERAL;
        }

        switch (currentChar) {
            case '+':
                takeIt();
                return Kind.PLUS;

            case '-':
                takeIt();
                return Kind.MINUS;

            case '*':
                takeIt();
                return Kind.TIMES;

            case '/':
                takeIt();
                return Kind.DIVIDE;

            case '<':
                takeIt();
                return Kind.LT;

            case '>':
                takeIt();
                return Kind.GT;

            case '=':
                takeIt();
                return Kind.EQ;

            case ';':
                takeIt();
                return Kind.SEMICOLON;

            case '.':
                takeIt();
                return Kind.DOT;

            case ',':
                takeIt();
                return Kind.COMMA;

            case '(':
                takeIt();
                return Kind.LPAREN;

            case ')':
                takeIt();
                return Kind.RPAREN;

            case ':':
                takeIt();
                if (currentChar == '=') {
                    takeIt();
                    return Kind.BECOMES;
                }
                return Kind.COLON;

            case '\000':
                return Kind.EOT;

            default:
                lexicalError(
                        "caractere inválido: " + currentChar
                );
                return null;
        }
    }

    public Token scan() {
        // ignora espaços/comentários
        scanSeparator();

        // inicia lexema
        currentSpelling = new StringBuilder();

        // salva posição inicial
        int tokenLine = line;
        int tokenColumn = column;

        // reconhece token
        Kind kind = scanToken();

        // cria token
        return new Token(kind, currentSpelling.toString(),
                tokenLine, tokenColumn);
    }

    public Object[][] scanAllAsMatrix() {
        List<Token> tokens = new ArrayList<>();
        Token currentToken;

        // Coleta todos os tokens até o fim do arquivo
        do {
            currentToken = scan();
            tokens.add(currentToken);
        } while (currentToken.kind != Kind.EOT);

        // Prepara a matriz n x 4
        Object[][] matrix = new Object[tokens.size()][4];

        // Preenche a matriz com os dados no formato: Tipo | Valor | Linha | Coluna
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            matrix[i][0] = t.getType(); // tipo (int)
            matrix[i][1] = t.lexeme;    // valor (String)
            matrix[i][2] = t.line;      // linha (int)
            matrix[i][3] = t.column;    // coluna (int)
        }

        return matrix;
    }
}