package br.edu.compiladorjava;

import br.edu.compiladorjava.lexer.Scanner;
import br.edu.compiladorjava.lexer.Token;
import br.edu.compiladorjava.lexer.Kind;
import br.edu.compiladorjava.lexer.LexerException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private App() {
    }

    public static void main(String[] args) {
        System.out.println(startupMessage());

        // Caminho do arquivo que você quer testar
        String nomeArquivo = "entrada.txt";

        // Uso de try-with-resources para fechar o arquivo automaticamente
        try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {

            Scanner scanner = new Scanner(reader);
            Token token;

            System.out.println("--- Iniciando Scanner no arquivo: " + nomeArquivo + " ---");

            do {
                token = scanner.scan();
                // Utiliza o toString() que você implementou na classe Token
                System.out.println(token);
            } while (token.kind != Kind.EOT);

            System.out.println("--- Fim da Análise Léxica ---");

        } catch (LexerException e) {
            // Erros de caracteres inválidos no código fonte
            System.err.println(e.getMessage());
        } catch (Exception e) {
            // Erros de sistema (arquivo não encontrado, permissão, etc)
            LOGGER.log(Level.SEVERE, "Erro crítico ao executar o compilador", e);
        }
    }

    public static String startupMessage() {
        return "Compilador Java UNIVASF";
    }
}