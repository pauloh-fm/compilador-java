package br.edu.compiladorjava.cli;

import br.edu.compiladorjava.lexer.Lexer;
import br.edu.compiladorjava.lexer.Token;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CompilerCli {

    public void run(String[] args, InputStream input, PrintStream output, Path workingDirectory) {
        try {
            Path sourceFile = selectSourceFile(args, input, output, workingDirectory);
            if (sourceFile == null) {
                return;
            }

            output.println("Arquivo selecionado: " + sourceFile.getFileName());
            String source = Files.readString(sourceFile, StandardCharsets.UTF_8);

            output.println("Iniciando analise lexica...");
            Lexer lexer = new Lexer(source, output::println);
            List<Token> tokens = lexer.scanTokens();

            output.println("Tokens finais:");
            for (Token token : tokens) {
                output.println(token);
            }
            output.println("Execucao concluida.");
        } catch (IOException | IllegalArgumentException exception) {
            output.println("Erro ao executar CLI: " + exception.getMessage());
        }
    }

    private Path selectSourceFile(String[] args, InputStream input, PrintStream output, Path workingDirectory)
            throws IOException {
        if (args != null && args.length > 0) {
            return resolvePath(args[0], workingDirectory);
        }

        List<Path> textFiles = listTextFiles(workingDirectory);
        if (textFiles.isEmpty()) {
            output.println("Nenhum arquivo .txt encontrado em " + workingDirectory.toAbsolutePath());
            return null;
        }

        output.println("Arquivos .txt encontrados:");
        for (int i = 0; i < textFiles.size(); i++) {
            output.println((i + 1) + ") " + textFiles.get(i).getFileName());
        }
        output.print("Escolha um arquivo pelo numero ou informe o caminho: ");
        output.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        String selection = reader.readLine();
        if (selection == null || selection.isBlank()) {
            output.println("Nenhum arquivo selecionado.");
            return null;
        }

        return resolveSelection(selection.trim(), textFiles, workingDirectory);
    }

    private List<Path> listTextFiles(Path workingDirectory) throws IOException {
        List<Path> textFiles = new ArrayList<>();
        try (var paths = Files.list(workingDirectory)) {
            paths.filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".txt"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase()))
                    .forEach(textFiles::add);
        }
        return textFiles;
    }

    private Path resolveSelection(String selection, List<Path> textFiles, Path workingDirectory) {
        if (selection.matches("\\d+")) {
            int index = Integer.parseInt(selection) - 1;
            if (index >= 0 && index < textFiles.size()) {
                return textFiles.get(index);
            }
        }

        return resolvePath(selection, workingDirectory);
    }

    private Path resolvePath(String value, Path workingDirectory) {
        Path path = Path.of(value);
        if (!path.isAbsolute()) {
            path = workingDirectory.resolve(path).normalize();
        }
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Arquivo nao encontrado: " + value);
        }
        return path;
    }
}