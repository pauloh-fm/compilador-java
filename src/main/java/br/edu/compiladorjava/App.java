package br.univasf.compiladorjava;

public final class App {

    private App() {
    }

    public static void main(String[] args) {
        System.out.println(startupMessage());
    }

    public static String startupMessage() {
        return "Compilador Java pronto para desenvolvimento.";
    }
}