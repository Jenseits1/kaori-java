package com.kaori.compiler.bytecode;

public record Instruction(Opcode opcode, Object operand) {
    @Override
    public String toString() {
        if (operand == null) {
            return String.format("%s", this.opcode);
        }
        return String.format("%s %s", this.opcode, this.operand);
    }
}
