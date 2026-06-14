package br.edu.compiladorjava.codegen;

public class LabelGenerator {
    private int counter = 0;

    public String nextLabel() {
        return "L" + counter++;
    }
}