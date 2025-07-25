package com.kaori.vm;

public class Instruction {
    public final InstructionKind kind;
    public final int operand;

    public static enum InstructionKind {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        MODULO,
        AND,
        OR,
        NOT_EQUAL,
        EQUAL,
        GREATER,
        GREATER_EQUAL,
        LESS,
        LESS_EQUAL,
        NOT,
        NEGATE,

        LOAD_LOCAL,
        LOAD_GLOBAL,
        STORE_LOCAL,
        STORE_GLOBAL,
        PUSH_CONST,
        JUMP_IF_FALSE,
        JUMP_IF_TRUE,
    }

    public Instruction(InstructionKind kind, int operand) {
        this.kind = kind;
        this.operand = operand;
    }
}
