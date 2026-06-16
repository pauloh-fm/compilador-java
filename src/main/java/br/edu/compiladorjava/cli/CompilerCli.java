package br.edu.compiladorjava.cli;

import br.edu.compiladorjava.Compiler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Interface de linha de comando (CLI) para o compilador.
 *
 * Suporta modo batch (compilar arquivo) e modo interativo.
 */
public class CompilerCli {

    private static final String VERSION = "1.0.0";
    private static final String PROMPT = "compilador> ";

    /**
     * Ponto de entrada da aplicação.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            interactiveMode();
        } else if (args.length >= 1) {
            batchMode(args);
        }
    }

    /**
     * Modo interativo.
     */
    private static void interactiveMode() {
        Scanner scanner = new Scanner(System.in);
        Compiler compiler = new Compiler(false);

        printWelcome();

        while (true) {
            System.out.print(PROMPT);
            String command = scanner.nextLine().trim();

            if (command.isEmpty()) {
                continue;
            }

            if (!executeCommand(command, compiler, scanner)) {
                break;
            }
        }

        scanner.close();
        System.out.println("\nAté logo!");
    }

    /**
     * Modo batch (compilar arquivo).
     */
    private static void batchMode(String[] args) {
        String filePath = args[0];
        boolean verbose = hasFlag(args, "--verbose");

        System.out.println("Compilando: " + filePath);

        try {
            String source = readFile(filePath);
            Compiler compiler = new Compiler(verbose);

            if (compiler.compile(source)) {
                System.out.println("✓ Compilação bem-sucedida!");
                System.exit(0);
            } else {
                System.out.println("✗ Compilação falhou!");
                if (!compiler.getErrorLog().isEmpty()) {
                    System.err.println("\nErros encontrados:");
                    System.err.println(compiler.getErrorLog());
                }
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Executa um comando no modo interativo.
     */
    private static boolean executeCommand(String command, Compiler compiler, Scanner scanner) {
        String[] parts = command.split("\\s+", 2);
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
            case "compile":
                handleCompile(parts.length > 1 ? parts[1] : null, compiler);
                break;

            case "interactive":
                handleInteractiveCompile(compiler, scanner);
                break;

            case "verbose":
                handleVerbose(compiler);
                break;

            case "help":
                printHelp();
                break;

            case "version":
                printVersion();
                break;

            case "info":
                printInfo();
                break;

            case "exit":
            case "quit":
                return false;

            default:
                System.out.println("Comando desconhecido: " + cmd);
                System.out.println("Digite 'help' para ver comandos disponíveis.");
        }

        return true;
    }

    /**
     * Compila um arquivo.
     */
    private static void handleCompile(String filePath, Compiler compiler) {
        if (filePath == null || filePath.isEmpty()) {
            System.out.println("Uso: compile <arquivo.tri>");
            return;
        }

        try {
            String source = readFile(filePath);
            System.out.println("Compilando: " + filePath);

            if (compiler.compile(source)) {
                System.out.println("✓ Compilação bem-sucedida!");
            } else {
                System.out.println("✗ Compilação falhou!");
                if (!compiler.getErrorLog().isEmpty()) {
                    System.out.println("\nErros:");
                    System.out.print(compiler.getErrorLog());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }

        compiler.reset();
    }

    /**
     * Modo de compilação interativa (digita código).
     */
    private static void handleInteractiveCompile(Compiler compiler, Scanner scanner) {
        System.out.println("Digite o código-fonte (terminar com uma linha contendo 'FIM'):");
        StringBuilder code = new StringBuilder();

        while (true) {
            String line = scanner.nextLine();
            if ("FIM".equalsIgnoreCase(line.trim())) {
                break;
            }
            code.append(line).append("\n");
        }

        if (code.length() > 0) {
            if (compiler.compile(code.toString())) {
                System.out.println("✓ Compilação bem-sucedida!");
            } else {
                System.out.println("✗ Compilação falhou!");
                System.out.println(compiler.getErrorLog());
            }
        }

        compiler.reset();
    }

    /**
     * Habilita modo verbose.
     */
    private static void handleVerbose(Compiler compiler) {
        System.out.println("Modo verbose ativado. Digite 'compile <arquivo>' para ver detalhes.");
    }

    /**
     * Lê um arquivo.
     */
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    /**
     * Verifica se uma flag está presente.
     */
    private static boolean hasFlag(String[] args, String flag) {
        for (String arg : args) {
            if (arg.equals(flag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mostra mensagem de boas-vindas.
     */
    private static void printWelcome() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  Compilador - Linguagem Customizada        ║");
        System.out.println("║  Versão " + VERSION + "                           ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        System.out.println("Digite 'help' para ver comandos disponíveis.");
    }

    /**
     * Mostra a ajuda.
     */
    private static void printHelp() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║              COMANDOS DISPONÍVEIS          ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        System.out.println("compile <arquivo>   - Compila um arquivo .tri");
        System.out.println("interactive         - Modo de entrada interativa");
        System.out.println("verbose             - Ativa modo verbose");
        System.out.println("help                - Mostra esta ajuda");
        System.out.println("version             - Mostra versão");
        System.out.println("info                - Mostra informações");
        System.out.println("exit, quit          - Sai do compilador\n");

        System.out.println("Exemplos:");
        System.out.println("  compile programa.tri");
        System.out.println("  interactive");
    }

    /**
     * Mostra versão.
     */
    private static void printVersion() {
        System.out.println("Compilador versão " + VERSION);
    }

    /**
     * Mostra informações.
     */
    private static void printInfo() {
        System.out.println(Compiler.getInfo());
    }
}
