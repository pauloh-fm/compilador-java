package br.edu.compiladorjava.parser;

import br.edu.compiladorjava.ast.*;
import br.edu.compiladorjava.lexer.Lexer;
import br.edu.compiladorjava.lexer.Token;
import br.edu.compiladorjava.lexer.TokenType;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.scan();
    }

    private void advance() {
        currentToken = lexer.scan();
    }

    private void accept(TokenType expected) {
        if (currentToken.getKind() != expected) {
            error("Token esperado: " + expected + ", mas recebeu: " + currentToken.getKind());
        }
        advance();
    }

    private void error(String message) {
        String fullMessage = message + " em linha " + currentToken.getLine() +
                            ", coluna " + currentToken.getColumn();
        throw new ParserException(fullMessage, currentToken.getLine(), currentToken.getColumn());
    }

    public ProgramNode parseProgram() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        if (currentToken.getKind() == TokenType.IDENTIFIER &&
            "program".equals(currentToken.getSpelling())) {
            advance();
        } else {
            error("'program' esperado");
        }

        String programName = currentToken.getSpelling();
        parseId();
        accept(TokenType.SEMICOLON);
        CorpoNode corpo = parseCorpo();

        if (currentToken.getKind() != TokenType.EOT) {
            error("Fim de programa esperado (.)");
        }

        return new ProgramNode(programName, corpo, line, column);
    }

    private CorpoNode parseCorpo() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        DeclarationsNode declarations = parseDeclaraciones();
        CommandBlockNode commandBlock = parseComandoComposto();

        return new CorpoNode(declarations, commandBlock, line, column);
    }

    private DeclarationsNode parseDeclaraciones() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        List<VariableDeclNode> declarations = new ArrayList<>();
        parseDeclResto(declarations);

        return new DeclarationsNode(declarations, line, column);
    }

    private void parseDeclResto(List<VariableDeclNode> declarations) {
        if (currentToken.getKind() == TokenType.VAR) {
            VariableDeclNode decl = parseDeclaracion();
            declarations.add(decl);
            accept(TokenType.SEMICOLON);
            parseDeclResto(declarations);
        }
    }

    private VariableDeclNode parseDeclaracion() {
        return parseDeclaracionDeVariavel();
    }

    private VariableDeclNode parseDeclaracionDeVariavel() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        accept(TokenType.VAR);
        String varName = currentToken.getSpelling();
        parseId();
        accept(TokenType.COLON);
        String type = parseTipo();

        return new VariableDeclNode(varName, type, line, column);
    }

    private String parseTipo() {
        return parseTipoSimple();
    }

    private String parseTipoSimple() {
        if (currentToken.getKind() == TokenType.IDENTIFIER) {
            String tipo = currentToken.getSpelling();
            if ("integer".equals(tipo) || "boolean".equals(tipo)) {
                advance();
                return tipo;
            } else {
                error("Tipo esperado (integer ou boolean), mas recebeu: " + tipo);
            }
        } else {
            error("Tipo esperado");
        }
        return null;
    }

    private CommandBlockNode parseComandoComposto() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        accept(TokenType.BEGIN);
        List<AstNode> commands = new ArrayList<>();
        parseListaDeComandos(commands);
        accept(TokenType.END);

        return new CommandBlockNode(commands, line, column);
    }

    private void parseListaDeComandos(List<AstNode> commands) {
        parseListaCmdResto(commands);
    }

    private void parseListaCmdResto(List<AstNode> commands) {
        if (isComandoStart()) {
            AstNode command = parseComando();
            commands.add(command);
            accept(TokenType.SEMICOLON);
            parseListaCmdResto(commands);
        }
    }

    private AstNode parseComando() {
        switch (currentToken.getKind()) {
            case IDENTIFIER:
                return parseAtribuicion();
            case IF:
                return parseCondicional();
            case WHILE:
                return parseIterativo();
            case BEGIN:
                return parseComandoComposto();
            default:
                error("Comando esperado");
                return null;
        }
    }

    private boolean isComandoStart() {
        return currentToken.getKind() == TokenType.IDENTIFIER ||
               currentToken.getKind() == TokenType.IF ||
               currentToken.getKind() == TokenType.WHILE ||
               currentToken.getKind() == TokenType.BEGIN;
    }

    private AstNode parseAtribuicion() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        String varName = currentToken.getSpelling();
        parseVariavel();
        accept(TokenType.BECOMES);
        AstNode expression = parseExpresion();

        return new AssignmentNode(varName, expression, line, column);
    }

    private AstNode parseCondicional() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        accept(TokenType.IF);
        AstNode condition = parseExpresion();
        accept(TokenType.THEN);
        AstNode thenCommand = parseComando();
        AstNode elseCommand = parseCondElse();

        return new ConditionalNode(condition, thenCommand, elseCommand, line, column);
    }

    private AstNode parseCondElse() {
        if (currentToken.getKind() == TokenType.ELSE) {
            advance();
            return parseComando();
        }
        return null;
    }

    private AstNode parseIterativo() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        accept(TokenType.WHILE);
        AstNode condition = parseExpresion();
        accept(TokenType.DO);
        AstNode command = parseComando();

        return new LoopNode(condition, command, line, column);
    }

    private void parseVariavel() {
        parseId();
    }

    private AstNode parseExpresion() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        AstNode left = parseExpresionSimple();

        if (isOperadorRel()) {
            String operator = currentToken.getSpelling();
            advance();
            AstNode right = parseExpresionSimple();
            return new BinaryOpNode(left, operator, right, line, column);
        }

        return left;
    }

    private AstNode parseExpresionSimple() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        AstNode left = parseTermo();

        while (isOperadorAd()) {
            String operator = currentToken.getSpelling();
            if (currentToken.getKind() == TokenType.PLUS) {
                operator = "+";
            } else if (currentToken.getKind() == TokenType.MINUS) {
                operator = "-";
            }
            advance();
            AstNode right = parseTermo();
            left = new BinaryOpNode(left, operator, right, line, column);
        }

        return left;
    }

    private AstNode parseTermo() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        AstNode left = parseFactor();

        while (isOperadorMul()) {
            String operator = currentToken.getSpelling();
            if (currentToken.getKind() == TokenType.MULTIPLY) {
                operator = "*";
            } else if (currentToken.getKind() == TokenType.DIVIDE) {
                operator = "/";
            }
            advance();
            AstNode right = parseFactor();
            left = new BinaryOpNode(left, operator, right, line, column);
        }

        return left;
    }

    private AstNode parseFactor() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();

        if (currentToken.getKind() == TokenType.IDENTIFIER) {
            String name = currentToken.getSpelling();
            parseVariavel();
            return new VariableNode(name, line, column);
        } else if (isLiteral()) {
            return parseLiteral();
        } else if (currentToken.getKind() == TokenType.LPAREN) {
            advance();
            AstNode expr = parseExpresion();
            accept(TokenType.RPAREN);
            return expr;
        } else if (currentToken.getKind() == TokenType.PLUS ||
                   currentToken.getKind() == TokenType.MINUS) {
            String operator = currentToken.getKind() == TokenType.PLUS ? "+" : "-";
            advance();
            AstNode operand = parseFactor();
            return new UnaryOpNode(operator, operand, line, column);
        } else {
            error("Fator esperado");
            return null;
        }
    }

    private AstNode parseLiteral() {
        int line = currentToken.getLine();
        int column = currentToken.getColumn();
        String value = currentToken.getSpelling();
        String type = "unknown";

        if (currentToken.getKind() == TokenType.INTLITERAL) {
            type = "int";
            advance();
        } else if (isBooleanoLit()) {
            type = "boolean";
            advance();
        } else {
            error("Literal esperado");
        }

        return new LiteralNode(value, type, line, column);
    }

    private boolean isBooleanoLit() {
        if (currentToken.getKind() == TokenType.IDENTIFIER) {
            String spelling = currentToken.getSpelling();
            return "true".equals(spelling) || "false".equals(spelling);
        }
        return false;
    }

    private boolean isLiteral() {
        return currentToken.getKind() == TokenType.INTLITERAL ||
               isBooleanoLit();
    }

    private void parseId() {
        if (currentToken.getKind() != TokenType.IDENTIFIER) {
            error("Identificador esperado");
        }
        advance();
    }

    private boolean isOperadorRel() {
        return currentToken.getKind() == TokenType.LESS_THAN ||
               currentToken.getKind() == TokenType.GREATER_THAN ||
               currentToken.getKind() == TokenType.EQUALS;
    }

    private boolean isOperadorAd() {
        if (currentToken.getKind() == TokenType.PLUS ||
            currentToken.getKind() == TokenType.MINUS) {
            return true;
        }
        if (currentToken.getKind() == TokenType.IDENTIFIER) {
            return "or".equals(currentToken.getSpelling());
        }
        return false;
    }

    private boolean isOperadorMul() {
        if (currentToken.getKind() == TokenType.MULTIPLY ||
            currentToken.getKind() == TokenType.DIVIDE) {
            return true;
        }
        if (currentToken.getKind() == TokenType.IDENTIFIER) {
            return "and".equals(currentToken.getSpelling());
        }
        return false;
    }

    public String getPosition() {
        return "linha " + currentToken.getLine() + ", coluna " + currentToken.getColumn();
    }
}
