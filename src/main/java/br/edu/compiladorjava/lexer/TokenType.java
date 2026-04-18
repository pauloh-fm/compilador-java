package br.edu.compiladorjava.lexer;

public enum TokenType {
    // Literais e Identificadores
    IDENTIFIER, // [a-zA-Z][a-zA-Z0-9]*
    INTLITERAL, // [0-9]+

    // Operadores
    OPERATOR, // +, -, *, /, <, >, =, \
    PLUS, // +
    MINUS, // -
    MULTIPLY, // *
    DIVIDE, // /
    LESS_THAN, // <
    GREATER_THAN, // >
    EQUALS, // =
    BACKSLASH, // \

    // Palavras-chave
    BEGIN, // begin
    CONST, // const
    DO, // do
    ELSE, // else
    END, // end
    IF, // if
    IN, // in
    LET, // let
    THEN, // then
    VAR, // var
    WHILE, // while

    // Separadores e Símbolos
    SEMICOLON, // ;
    COLON, // :
    BECOMES, // :=
    IS, // ~
    LPAREN, // (
    RPAREN, // )

    // Fim de arquivo
    EOT // End of Text
}
