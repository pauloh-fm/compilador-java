package br.edu.compiladorjava.parser;

import br.edu.compiladorjava.ast.*;
import br.edu.compiladorjava.lexer.*;

public class Parser {
    private final Scanner scanner;
    private Token currentToken;
    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.currentToken = scanner.scan(); // Inicializa o lookahead
    }

    private void accept(Kind expectedKind) {
        if (currentToken.kind == expectedKind) {
            currentToken = scanner.scan();
        } else {
            throw new ParserException("Esperava " + expectedKind.name() +
                    " mas encontrou " + currentToken.kind.name(), currentToken);
        }
    }

    private void acceptIt() {
        currentToken = scanner.scan();
    }

    // <programa> ::= program <id> ; <corpo> .
    public ProgramNode parse() {
        ProgramNode program = parsePrograma();
        if (currentToken.kind != Kind.EOT) {
            throw new ParserException("Tokens inesperados no final do arquivo.", currentToken);
        }
        return program;
    }

    private ProgramNode parsePrograma() {
        ProgramNode p = new ProgramNode();
        accept(Kind.PROGRAM);
        accept(Kind.IDENTIFIER);
        accept(Kind.SEMICOLON);
        p.declarations = parseDeclaracoes();
        p.commands = parseComandoComposto();
        accept(Kind.DOT);
        return p;
    }

    // <corpo> ::= <declarações> <comando-composto>
    private void parseCorpo() {
        parseDeclaracoes();
        parseComandoComposto();
    }

    // <declarações> ::= <declaração> ; <declarações> | <vazio>
    private DeclarationNode parseDeclaracoes() {
        DeclarationNode first = null;
        DeclarationNode last = null;
        while (currentToken.kind == Kind.VAR) {
            DeclarationNode d = parseDeclaracao();
            if (first == null) {
                first = d;
            } else {
                last.next = d;
            }
            last = d;
            accept(Kind.SEMICOLON);
        }
        return first;
    }

    // <declaração> ::= var <id> : <tipo>
    private DeclarationNode parseDeclaracao() {
        accept(Kind.VAR);

        String nome = currentToken.lexeme;
        int line = currentToken.line;
        int column = currentToken.column;

        accept(Kind.IDENTIFIER);
        accept(Kind.COLON);
        String tipo = currentToken.lexeme;
        parseTipo();

        return new DeclarationNode(nome, tipo, line, column);
    }

    // <tipo> ::= integer | boolean
    private void parseTipo() {
        if (currentToken.kind == Kind.INTEGER || currentToken.kind == Kind.BOOLEAN) {
            acceptIt();
        } else {
            throw new ParserException("Tipo esperado (integer ou boolean)", currentToken);
        }
    }

    // <comando-composto> ::= begin <lista-de-comandos> end
    private CommandNode parseComandoComposto() {
        accept(Kind.BEGIN);
        CommandNode cmds = parseListaDeComandos();
        accept(Kind.END);
        return cmds;
    }

    // <lista-de-comandos> ::= <comando> ; <lista-de-comandos> | <vazio>
    private CommandNode parseListaDeComandos() {
        CommandNode first = null;
        CommandNode last = null;

        while (isComandoStart()) {
            CommandNode cmd = parseComando();
            if (cmd != null) {
                if (first == null) {
                    first = cmd;
                } else {
                    last.next = cmd;
                }
                last = cmd;
            }
            accept(Kind.SEMICOLON);
        }
        return first;
    }

    private boolean isComandoStart() {
        return currentToken.kind == Kind.IDENTIFIER || currentToken.kind == Kind.IF ||
                currentToken.kind == Kind.WHILE || currentToken.kind == Kind.BEGIN;
    }

    // <comando> ::= <atribuição> | <condicional> | <iterativo> | <comando-composto>
    private CommandNode parseComando() {
        switch (currentToken.kind) {
            case IDENTIFIER:
                return parseAtribuicao();
            case IF:
                parseCondicional();
                return null;
            case WHILE:
                parseIterativo();
                return null;
            case BEGIN:
                return parseComandoComposto();
            default:
                throw new ParserException("Comando inválido esperado", currentToken);
        }
    }

    // <atribuição> ::= <variável> := <expressão>
    private AssignmentNode parseAtribuicao() {
        String nome = currentToken.lexeme;
        int line = currentToken.line;
        int column = currentToken.column;
        accept(Kind.IDENTIFIER);
        accept(Kind.BECOMES);
        ExpressionNode expr = parseExpressao();

        return new AssignmentNode(nome, expr, line, column);
    }

    // <condicional> ::= if <expressão> then <comando> ( else <comando> | <vazio> )
    private void parseCondicional() {
        accept(Kind.IF);
        parseExpressao();
        accept(Kind.THEN);
        parseComando();
        if (currentToken.kind == Kind.ELSE) {
            acceptIt();
            parseComando();
        }
    }

    // <iterativo> ::= while <expressão> do <comando>
    private void parseIterativo() {
        accept(Kind.WHILE);
        parseExpressao();
        accept(Kind.DO);
        parseComando();
    }

    // <expressão> ::= <expressão-simples> ( <op-rel> <expressão-simples> | <vazio> )
    private ExpressionNode parseExpressao() {
        ExpressionNode left = parseExpressaoSimples();
        if (isOpRel()) {
            int line = currentToken.line;
            int column = currentToken.column;

            String op = currentToken.lexeme;
            acceptIt();

            ExpressionNode right = parseExpressaoSimples();

            left = new BinaryExpressionNode(op, left, right, line, column);
        }
        return left;
    }

    private boolean isOpRel() {
        return currentToken.kind == Kind.LT || currentToken.kind == Kind.GT || currentToken.kind == Kind.EQ;
    }

    // <expressão-simples> ::= <termo> ( <op-ad> <termo> )*
    private ExpressionNode parseExpressaoSimples() {
        ExpressionNode left = parseTermo();
        while (currentToken.kind == Kind.PLUS || currentToken.kind == Kind.MINUS || currentToken.kind == Kind.OR) {
            int line = currentToken.line;
            int column = currentToken.column;

            String op = currentToken.lexeme;
            acceptIt();

            ExpressionNode right = parseTermo();

            left = new BinaryExpressionNode(op, left, right, line, column);
        }
        return left;
    }

    private boolean isOpAd() {
        return currentToken.kind == Kind.PLUS || currentToken.kind == Kind.MINUS || currentToken.kind == Kind.OR;
    }

    // <termo> ::= <fator> ( <op-mul> <fator> )*
    private ExpressionNode parseTermo() {
        ExpressionNode left = parseFator();
        while (currentToken.kind == Kind.TIMES || currentToken.kind == Kind.DIVIDE || currentToken.kind == Kind.AND) {
            int line = currentToken.line;
            int column = currentToken.column;
            String op = currentToken.lexeme;
            acceptIt();

            ExpressionNode right = parseFator();

            left = new BinaryExpressionNode(op, left, right, line, column);
        }
        return left;
    }

    private boolean isOpMul() {
        return currentToken.kind == Kind.TIMES || currentToken.kind == Kind.DIVIDE || currentToken.kind == Kind.AND;
    }

    // <fator> ::= <variável> | <literal> | "(" <expressão> ")"
    private ExpressionNode parseFator() {
        if (currentToken.kind == Kind.IDENTIFIER) {
            String nome = currentToken.lexeme;
            int line = currentToken.line;
            int column = currentToken.column;
            acceptIt();
            return new IdentifierNode(nome, line, column);
        }
        if (currentToken.kind == Kind.INTLITERAL) {
            String valor = currentToken.lexeme;
            int line = currentToken.line;
            int column = currentToken.column;
            acceptIt();

            return new LiteralNode(valor, line, column);
        }
        if (currentToken.kind == Kind.TRUE || currentToken.kind == Kind.FALSE) {
            String valor = currentToken.lexeme;
            int line = currentToken.line;
            int column = currentToken.column;
            acceptIt();

            return new LiteralNode(valor, line, column);
        }
        if (currentToken.kind == Kind.LPAREN) {
            acceptIt();
            ExpressionNode expr = parseExpressao();
            accept(Kind.RPAREN);
            return expr;
        }
        throw new ParserException(
                "Fator inválido.",
                currentToken
        );
    }
}