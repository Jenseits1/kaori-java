package com.kaori.compiler.bytecode;

public class Instruction {
    private final Opcode opcode;
    private Object operand;

    public Instruction(
            Opcode opcode, Object operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    @Override
    public String toString() {
        if (operand == null) {
            return String.format("%s", this.opcode);
        }
        return String.format("%s %s", this.opcode, this.operand);
    }

    public Opcode opcode() {
        return this.opcode;
    }

    public Object operand() {
        return this.operand;
    }

    public void setOperand(Object operand) {
        this.operand = operand;
    }
}
