package br.edu.compiladorjava.lexer;

/**
 * Analisador Léxico (Scanner) para a linguagem Mini-Triangle.
 *
 * Implementação baseada em "Programming Language Processors in Java" (Watt & Brown).
 *
 * Gramática Léxica:
 * Token         ::= Identifier | Integer-Literal | Operator
 *                 | begin | const | do | else | end | if | in
 *                 | let | then | var | while
 *                 | ; | : | := | ~ | ( | ) | eot
 *
 * Identifier    ::= Letter (Letter | Digit)*
 * Integer-Literal ::= Digit Digit*
 * Operator      ::= + | - | * | / | < | > | = | \
 * Comment       ::= ! Graphic* eol
 */
public class Lexer {
    private final String source;
    private int currentPos;
    private char currentChar;
    private int line;
    private int column;
    private StringBuilder currentSpelling;

    /**
     * Cria um novo lexer para o código fornecido.
     *
     * @param source código-fonte a ser analisado
     */
    public Lexer(String source) {
        this.source = source;
        this.currentPos = 0;
        this.line = 1;
        this.column = 0;
        this.currentSpelling = new StringBuilder();
        // Inicializa o primeiro caractere sem contar como avanço
        currentChar = (source.length() > 0) ? source.charAt(0) : '\u0000';
        column = 1;
    }

    /**
     * Avança para o próximo caractere e atualiza linha/coluna.
     */
    private void advance() {
        if (currentChar == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }

        currentPos++;
        currentChar = (currentPos < source.length()) ? source.charAt(currentPos) : '\u0000';
    }

    /**
     * Adiciona o caractere atual ao lexema e avança.
     */
    private void takeIt() {
        currentSpelling.append(currentChar);
        advance();
    }

    /**
     * Verifica se um caractere é uma letra.
     */
    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * Verifica se um caractere é um dígito.
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Verifica se um caractere é um espaço em branco.
     */
    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    /**
     * Pula espaços em branco e comentários.
     */
    private void skipSeparators() {
        while (isWhitespace(currentChar) || currentChar == '!') {
            if (currentChar == '!') {
                // Pula comentário até o fim da linha
                advance(); // pula !
                while (currentChar != '\n' && currentChar != '\u0000') {
                    advance();
                }
                if (currentChar == '\n') {
                    advance(); // pula a quebra de linha
                }
            } else {
                advance();
            }
        }
    }

    /**
     * Escaneia um identificador ou palavra-chave.
     */
    private Token scanIdentifier() {
        int startLine = line;
        int startColumn = column;
        currentSpelling = new StringBuilder();

        // Primeiro caractere (letra)
        takeIt();

        // Caracteres subsequentes (letra ou dígito)
        while (isLetter(currentChar) || isDigit(currentChar)) {
            takeIt();
        }

        String spelling = currentSpelling.toString();
        TokenType kind = checkKeyword(spelling);

        return new Token(kind, spelling, startLine, startColumn);
    }

    /**
     * Escaneia um literal inteiro.
     */
    private Token scanInteger() {
        int startLine = line;
        int startColumn = column;
        currentSpelling = new StringBuilder();

        // Todos os dígitos
        while (isDigit(currentChar)) {
            takeIt();
        }

        return new Token(TokenType.INTLITERAL, currentSpelling.toString(), startLine, startColumn);
    }

    /**
     * Verifica se uma string é uma palavra-chave.
     */
    private TokenType checkKeyword(String spelling) {
        switch (spelling) {
            case "begin":  return TokenType.BEGIN;
            case "const":  return TokenType.CONST;
            case "do":     return TokenType.DO;
            case "else":   return TokenType.ELSE;
            case "end":    return TokenType.END;
            case "if":     return TokenType.IF;
            case "in":     return TokenType.IN;
            case "let":    return TokenType.LET;
            case "then":   return TokenType.THEN;
            case "var":    return TokenType.VAR;
            case "while":  return TokenType.WHILE;
            default:       return TokenType.IDENTIFIER;
        }
    }

    /**
     * Escaneia um operador ou símbolo especial.
     */
    private Token scanOperatorOrSymbol() {
        int startLine = line;
        int startColumn = column;
        currentSpelling = new StringBuilder();

        char ch = currentChar;

        switch (ch) {
            case '+':
                takeIt();
                return new Token(TokenType.PLUS, "+", startLine, startColumn);
            case '-':
                takeIt();
                return new Token(TokenType.MINUS, "-", startLine, startColumn);
            case '*':
                takeIt();
                return new Token(TokenType.MULTIPLY, "*", startLine, startColumn);
            case '/':
                takeIt();
                return new Token(TokenType.DIVIDE, "/", startLine, startColumn);
            case '<':
                takeIt();
                return new Token(TokenType.LESS_THAN, "<", startLine, startColumn);
            case '>':
                takeIt();
                return new Token(TokenType.GREATER_THAN, ">", startLine, startColumn);
            case '=':
                takeIt();
                return new Token(TokenType.EQUALS, "=", startLine, startColumn);
            case '\\':
                takeIt();
                return new Token(TokenType.BACKSLASH, "\\", startLine, startColumn);
            case ';':
                takeIt();
                return new Token(TokenType.SEMICOLON, ";", startLine, startColumn);
            case ':':
                takeIt();
                if (currentChar == '=') {
                    takeIt();
                    return new Token(TokenType.BECOMES, ":=", startLine, startColumn);
                }
                return new Token(TokenType.COLON, ":", startLine, startColumn);
            case '~':
                takeIt();
                return new Token(TokenType.IS, "~", startLine, startColumn);
            case '(':
                takeIt();
                return new Token(TokenType.LPAREN, "(", startLine, startColumn);
            case ')':
                takeIt();
                return new Token(TokenType.RPAREN, ")", startLine, startColumn);
            default:
                // Caractere inválido
                String invalid = String.valueOf(ch);
                takeIt();
                throw new LexerException("Caractere inválido: '" + invalid + "'", startLine, startColumn);
        }
    }

    /**
     * Obtém o próximo token do código-fonte.
     *
     * @return o próximo token
     */
    public Token scan() {
        // Pula separadores (espaços, comentários)
        skipSeparators();

        // Fim do arquivo
        if (currentChar == '\u0000') {
            return new Token(TokenType.EOT, "", line, column);
        }

        // Identificador ou palavra-chave
        if (isLetter(currentChar)) {
            return scanIdentifier();
        }

        // Literal inteiro
        if (isDigit(currentChar)) {
            return scanInteger();
        }

        // Operador ou símbolo
        return scanOperatorOrSymbol();
    }

    /**
     * Obtém a posição atual (para diagnósticos).
     */
    public String getPosition() {
        return String.format("linha %d, coluna %d", line, column);
    }
}
