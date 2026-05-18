package br.edu.compiladorjava;

import br.edu.compiladorjava.cli.CompilerCli;
import br.edu.compiladorjava.lexer.LexerException;
import br.edu.compiladorjava.parser.ParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class App {

    private App() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(startupMessage());
            return;
        }

        try {
            new CompilerCli().run(readSource(args));
        } catch (LexerException | ParserException | IOException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }

    public static String startupMessage() {
        return "Compilador Java UNIVASF";
    }

    private static String readSource(String[] args) throws IOException {
        if (args.length == 1) {
            Path path = Path.of(args[0]);
            if (Files.exists(path) && Files.isRegularFile(path)) {
                return Files.readString(path);
            }
        }

        return String.join(" ", args);
    }
}