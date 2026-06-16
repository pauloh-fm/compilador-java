package br.edu.compiladorjava;

import br.edu.compiladorjava.ast.AstPrinter;
import br.edu.compiladorjava.ast.JsonVisitor;
import br.edu.compiladorjava.ast.ProgramNode;
import br.edu.compiladorjava.lexer.Lexer;
import br.edu.compiladorjava.lexer.Token;
import br.edu.compiladorjava.lexer.TokenType;
import br.edu.compiladorjava.parser.Parser;
import br.edu.compiladorjava.parser.ParserException;
import br.edu.compiladorjava.lexer.LexerException;

/**
 * Compilador integrado para a linguagem customizada.
 *
 * Coordena as fases de análise léxica, sintática e construção de AST.
 */
public class Compiler {
    private String source;
    private boolean verbose;
    private boolean hasErrors;
    private StringBuilder errorLog;
    private ProgramNode ast;

    /**
     * Cria um novo compilador.
     *
     * @param verbose modo verbose (mostra tokens e informações de debug)
     */
    public Compiler(boolean verbose) {
        this.verbose = verbose;
        this.hasErrors = false;
        this.errorLog = new StringBuilder();
        this.ast = null;
    }

    /**
     * Compila o código-fonte fornecido.
     *
     * @param source código-fonte a compilar
     * @return true se compilação foi bem-sucedida, false caso contrário
     */
    public boolean compile(String source) {
        this.source = source;
        this.hasErrors = false;
        this.errorLog = new StringBuilder();
        this.ast = null;

        try {
            // Fase 1: Análise Léxica
            if (verbose) {
                System.out.println("=== FASE 1: ANÁLISE LÉXICA ===");
            }

            Lexer lexer = new Lexer(source);

            if (verbose) {
                printTokens(lexer);
                lexer = new Lexer(source);
            }

            // Fase 2: Análise Sintática
            if (verbose) {
                System.out.println("\n=== FASE 2: ANÁLISE SINTÁTICA ===");
            }

            Parser parser = new Parser(lexer);
            this.ast = parser.parseProgram();

            // Fase 3: Montagem de AST
            if (verbose) {
                System.out.println("\n=== FASE 3: MONTAGEM DE AST ===");
                System.out.println("✓ AST construída com sucesso!");
                showAst();
            }

            if (verbose) {
                System.out.println("\n✓ Compilação bem-sucedida!");
            }

            return true;

        } catch (LexerException e) {
            logError("ERRO LÉXICO: " + e.getMessage());
            return false;

        } catch (ParserException e) {
            logError("ERRO SINTÁTICO: " + e.getMessage());
            return false;

        } catch (Exception e) {
            logError("ERRO INTERNO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mostra todos os tokens para debug.
     */
    private void printTokens(Lexer lexer) {
        System.out.println("\nTokens gerados:");
        System.out.println("┌─────────────────────────────────────────────┐");

        Token token;
        int count = 0;

        do {
            token = lexer.scan();
            System.out.printf("  %2d: %-20s | '%s' @ Linha %d, Col %d\n",
                    ++count,
                    token.getKind(),
                    token.getSpelling(),
                    token.getLine(),
                    token.getColumn());
        } while (token.getKind() != TokenType.EOT);

        System.out.println("└─────────────────────────────────────────────┘");
    }

    /**
     * Mostra a AST para debug (privado).
     */
    private void showAst() {
        if (ast != null) {
            AstPrinter printer = new AstPrinter();
            String astStr = printer.print(ast);
            System.out.println("\nÁrvore Sintática (AST):");
            System.out.println("┌─────────────────────────────────────────────┐");
            System.out.print(astStr);
            System.out.println("└─────────────────────────────────────────────┘");
        }
    }

    /**
     * Registra um erro.
     */
    private void logError(String message) {
        this.hasErrors = true;
        errorLog.append(message).append("\n");
        System.err.println("❌ " + message);
    }

    /**
     * Verifica se há erros.
     */
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * Obtém o log de erros.
     */
    public String getErrorLog() {
        return errorLog.toString();
    }

    /**
     * Obtém a AST gerada.
     */
    public ProgramNode getAst() {
        return ast;
    }

    /**
     * Imprime a AST.
     */
    public String printAst() {
        if (ast != null) {
            AstPrinter printer = new AstPrinter();
            return printer.print(ast);
        }
        return "AST não disponível";
    }

    /**
     * Exporta a AST em formato JSON.
     */
    public String exportAsJson() {
        if (ast != null) {
            JsonVisitor visitor = new JsonVisitor();
            return visitor.toJson(ast);
        }
        return "{}";
    }

    /**
     * Limpa o compilador para reutilização.
     */
    public void reset() {
        this.hasErrors = false;
        this.errorLog = new StringBuilder();
        this.source = null;
        this.ast = null;
    }

    /**
     * Obtém a versão do compilador.
     */
    public static String getVersion() {
        return "1.0.0-Etapa3";
    }

    /**
     * Obtém informações do compilador.
     */
    public static String getInfo() {
        return "Compilador para Linguagem Customizada\n" +
                "Versão: " + getVersion() + "\n" +
                "Etapas: Análise Léxica + Análise Sintática (LL(1)) + AST";
    }
}
