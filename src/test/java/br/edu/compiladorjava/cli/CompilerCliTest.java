package br.edu.compiladorjava.cli;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CompilerCliTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldSelectTextFileAndShowLexicalProcess() throws Exception {
        Files.writeString(tempDir.resolve("notes.txt"), "ignored", StandardCharsets.UTF_8);
        Files.writeString(tempDir.resolve("language.txt"), "print 123 + foo", StandardCharsets.UTF_8);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompilerCli cli = new CompilerCli();

        cli.run(new String[0], new ByteArrayInputStream("1\n".getBytes(StandardCharsets.UTF_8)),
                new PrintStream(output, true, StandardCharsets.UTF_8), tempDir);

        String text = output.toString(StandardCharsets.UTF_8);

        assertTrue(text.contains("Arquivos .txt encontrados:"));
        assertTrue(text.contains("1) language.txt"));
        assertTrue(text.contains("Arquivo selecionado: language.txt"));
        assertTrue(text.contains("Iniciando analise lexica..."));
        assertTrue(text.contains("Token: PRINT print null"));
        assertTrue(text.contains("Token: NUMBER 123 123.0"));
        assertTrue(text.contains("Token: PLUS + null"));
        assertTrue(text.contains("Token: IDENTIFIER foo null"));
        assertTrue(text.contains("Execucao concluida."));
    }
}