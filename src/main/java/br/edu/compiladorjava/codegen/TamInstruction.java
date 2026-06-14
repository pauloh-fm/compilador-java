package br.edu.compiladorjava.codegen;

public class TamInstruction {
    private final String opcode;
    private final String operand;

    public TamInstruction(String opcode) {
        this(opcode, null);
    }

    public TamInstruction(String opcode, String operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    @Override
    public String toString() {
        if (operand == null) {
            return opcode;
        }
        return opcode + " " + operand;
    }
}