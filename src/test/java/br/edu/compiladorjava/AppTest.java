package br.edu.compiladorjava;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {

    @Test
    void startupMessageShouldDescribeTheProjectState() {
        assertEquals("Compilador Java pronto para desenvolvimento.", App.startupMessage());
    }
}