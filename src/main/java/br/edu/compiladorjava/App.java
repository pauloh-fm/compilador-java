package br.edu.compiladorjava;

import br.edu.compiladorjava.ast.*;
import br.edu.compiladorjava.lexer.*;
import br.edu.compiladorjava.parser.*;
import br.edu.compiladorjava.semantic.ContextAnalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class App {
    private static final String ARQUIVO = "entrada.txt";

    public static String startupMessage() {
        return "Compilador Java UNIVASF - Modo Sintático Direto";
    }

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        System.out.println("""
                1 - Apenas Léxico
                2 - Léxico + Sintático
                3 - Léxico + Sintático + AST
                4 - Léxico + Sintático + AST + Contexto
                """);

        System.out.print("Escolha uma opção: ");
        int opcao = teclado.nextInt();

        try {

            // ==================================================
            // ETAPA 1 - LÉXICO
            // ==================================================
            if (opcao >= 1) {

                System.out.println("\n=== ANÁLISE LÉXICA ===");

                try (BufferedReader br =
                             new BufferedReader(new FileReader(ARQUIVO))) {

                    br.edu.compiladorjava.lexer.Scanner scanner =
                            new br.edu.compiladorjava.lexer.Scanner(br);

                    Token token;

                    do {
                        token = scanner.scan();
                        System.out.println(token);
                    } while (token.kind != Kind.EOT);
                }

                if (opcao == 1) {
                    System.out.println("\n>>> Análise léxica concluída.");
                    return;
                }
            }

            // ==================================================
            // ETAPA 2+ - NOVO SCANNER
            // ==================================================
            ProgramNode ast;

            try (BufferedReader br =
                         new BufferedReader(new FileReader(ARQUIVO))) {

                br.edu.compiladorjava.lexer.Scanner scanner =
                        new br.edu.compiladorjava.lexer.Scanner(br);

                Parser parser = new Parser(scanner);

                ast = parser.parse();

                System.out.println(
                        "\n>>> SUCESSO: Programa sintaticamente correto!");
            }

            if (opcao == 2) {
                return;
            }

            // ==================================================
            // ETAPA 3 - AST
            // ==================================================
            if (opcao >= 3) {

                System.out.println("\n=== AST ===");

                AstPrinter printer = new AstPrinter();
                printer.print(ast);

                if (opcao == 3) {
                    return;
                }
            }

            // ==================================================
            // ETAPA 4 - CONTEXTO
            // ==================================================
            if (opcao >= 4) {

                System.out.println("\n=== ANÁLISE DE CONTEXTO ===");

                ContextAnalyzer analyzer = new ContextAnalyzer();
                analyzer.analyze(ast);

                System.out.println(
                        "\n>>> SUCESSO: Análise semântica concluída.");
            }

        } catch (LexerException e) {

            System.err.println("[ERRO LÉXICO] " + e.getMessage());

        } catch (ParserException e) {

            System.err.println("[ERRO SINTÁTICO] " + e.getMessage());

        } catch (IOException e) {

            System.err.println("[ERRO DE LEITURA] " + e.getMessage());
        }
    }
}