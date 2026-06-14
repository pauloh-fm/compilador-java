package br.edu.compiladorjava.codegen;

import br.edu.compiladorjava.ast.*;
import br.edu.compiladorjava.visitor.Visitor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CodeGenerator implements Visitor {
    private final List<TamInstruction> code = new ArrayList<>();
    private final Map<String, Integer> addresses = new HashMap<>();
    private int nextAddress = 0;
    public List<TamInstruction> generate(ProgramNode program) {
        program.visit(this);
        emit("HALT");
        return code;
    }
    private void emit(String opcode) {
        code.add(new TamInstruction(opcode));
    }
    private void emit(String opcode, String operand) {
        code.add(new TamInstruction(opcode, operand));
    }

    @Override
    public void visitProgramNode(ProgramNode p) {
        if (p.declarations != null) {
            p.declarations.visit(this);
        }
        if (p.commands != null) {
            p.commands.visit(this);
        }
    }

    @Override
    public void visitDeclarationNode(DeclarationNode d) {
        DeclarationNode current = d;
        while (current != null) {
            addresses.put(current.name, nextAddress++);
            current = current.next;
        }
    }

    @Override
    public void visitAssignmentNode(AssignmentNode a) {
        AssignmentNode current = a;
        while (current != null) {
            current.expression.visit(this);
            int address = addresses.get(current.variable);
            emit("STORE", String.valueOf(address));
            current = (AssignmentNode) current.next;
        }
    }

    @Override
    public void visitIdentifierNode(IdentifierNode i) {
        int address = addresses.get(i.name);
        emit("LOAD", String.valueOf(address));
    }

    @Override
    public void visitLiteralNode(LiteralNode l) {
        if ("true".equals(l.value)) {
            emit("LOADL", "1");
        }
        else if ("false".equals(l.value)) {
            emit("LOADL", "0");
        }
        else {
            emit("LOADL", l.value);
        }
    }

    @Override
    public void visitBinaryExpressionNode(BinaryExpressionNode e) {

        e.left.visit(this);
        e.right.visit(this);

        switch (e.operator) {

            // Operações aritméticas
            case "+":
                emit("CALL", "ADD");
                break;

            case "-":
                emit("CALL", "SUB");
                break;

            case "*":
                emit("CALL", "MULT");
                break;

            case "/":
                emit("CALL", "DIV");
                break;

            // Operações relacionais
            case "=":
                emit("CALL", "EQ");
                break;

            case "<":
                emit("CALL", "LT");
                break;

            case ">":
                emit("CALL", "GT");
                break;

            case "<=":
                emit("CALL", "LE");
                break;

            case ">=":
                emit("CALL", "GE");
                break;

            case "<>":
                emit("CALL", "NE");
                break;

            // Operações lógicas
            case "and":
                emit("CALL", "AND");
                break;

            case "or":
                emit("CALL", "OR");
                break;

            default:
                throw new RuntimeException("Operador não suportado: " + e.operator);
        }
    }

    public void saveToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (TamInstruction instruction : code) {
                writer.write(instruction.toString());
                writer.newLine();
            }
        }
    }
}