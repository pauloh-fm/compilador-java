package br.edu.compiladorjava.lexer;

public class Token {
    private final TokenType kind;
    private final String spelling;
    private final int line;
    private final int column;

    /**
     * Cria um novo token.
     *
     * @param kind     tipo do token
     * @param spelling lexema (texto original)
     * @param line     número da linha
     * @param column   número da coluna
     */
    public Token(TokenType kind, String spelling, int line, int column) {
        this.kind = kind;
        this.spelling = spelling;
        this.line = line;
        this.column = column;
    }

    /**
     * Obter o tipo do token.
     */
    public TokenType getKind() {
        return kind;
    }

    /**
     * Obter o lexema (texto original do token).
     */
    public String getSpelling() {
        return spelling;
    }

    /**
     * Obter o número da linha.
     */
    public int getLine() {
        return line;
    }

    /**
     * Obter o número da coluna.
     */
    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return String.format("Token [type=%s, spelling='%s', line=%d, column=%d]",
                kind, spelling, line, column);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Token))
            return false;
        Token other = (Token) obj;
        return kind == other.kind && spelling.equals(other.spelling);
    }

    @Override
    public int hashCode() {
        return kind.hashCode() * 31 + spelling.hashCode();
    }
}
