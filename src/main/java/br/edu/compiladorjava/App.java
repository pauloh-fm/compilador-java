package br.edu.compiladorjava;

import br.edu.compiladorjava.ast.*;
import br.edu.compiladorjava.lexer.*;
import br.edu.compiladorjava.parser.*;
import br.edu.compiladorjava.semantic.ContextAnalyzer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App {

    public static String startupMessage() {
        return "Compilador Java UNIVASF - Modo Sintático Direto";
    }

    public static void main(String[] args) {
        String caminhoArquivo = "entrada.txt";

        System.out.println(startupMessage());

        // Abre o arquivo apenas UMA vez usando o try-with-resources
        try (BufferedReader fileReader = new BufferedReader(new FileReader(caminhoArquivo))) {
            System.out.println("\n=== INICIANDO COMPILAÇÃO: " + caminhoArquivo + " ===");

            // 1. Instancia o Analisador Léxico (Scanner) acoplado ao arquivo
            Scanner scanner = new Scanner(fileReader);
//            Token token;
//            do {
//                token = scanner.scan();
//                System.out.println(token);
//            } while (token.kind != Kind.EOT);

            // 2. Instancia o Analisador Sintático (Parser) passando o scanner.
            // O construtor do Parser fará a primeira chamada a scanner.scan() para iniciar o lookahead.
            Parser parser = new Parser(scanner);

            // 3. O Parser assume o controle e vai chamando o léxico recursivamente/sequencialmente
            ProgramNode ast = parser.parse();

            AstPrinter printer = new AstPrinter();
            printer.print(ast);

            // Se o método acima terminar sem exceções, o código fonte é válido
            System.out.println("\n>>> SUCESSO: O programa está sintaticamente correto!\n" +
                    "==============================================================");

            // 4. Analisador de contexto
            ContextAnalyzer analyzer = new ContextAnalyzer();
            analyzer.analyze(ast);

            System.out.println("\n>>> SUCESSO: Análise semântica concluída sem erros de contexto.");

        } catch (LexerException e) {
            // Captura erros de caracteres inválidos interceptados durante o parsing
            System.err.println("\n[ERRO LÉXICO] " + e.getMessage());
        } catch (ParserException e) {
            // Captura desvios das regras gramaticais da linguagem
            System.err.println("\n[ERRO SINTÁTICO] " + e.getMessage());
        } catch (IOException e) {
            System.err.println("\n[ERRO DE LEITURA] Falha ao abrir ou ler o arquivo: " + e.getMessage());
        }
    }
}