package br.edu.compiladorjava;

import br.edu.compiladorjava.cli.CompilerCli;
import java.nio.file.Path;

public final class App {

    private App() {
    }

    public static void main(String[] args) {
        new CompilerCli().run(args, System.in, System.out, Path.of("."));
    }

    public static String startupMessage() {
        return "Compilador Java pronto";
    }
}