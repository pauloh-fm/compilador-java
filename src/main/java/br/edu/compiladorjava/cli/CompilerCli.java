package br.edu.compiladorjava.cli;

import br.edu.compiladorjava.lexer.Lexer;
import br.edu.compiladorjava.lexer.Token;
import br.edu.compiladorjava.parser.Parser;

import java.util.List;

public class CompilerCli {

    public void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }

        new Parser(tokens).parse();
        System.out.println("Analise lexica e sintatica concluida com sucesso.");
    }
}
