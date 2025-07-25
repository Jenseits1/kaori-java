package com.kaori.compiler.bytecode;

public enum Opcode {
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

    DECLARE,
    LOAD_LOCAL,
    LOAD_GLOBAL,
    STORE_LOCAL,
    STORE_GLOBAL,

    PUSH_CONST,

    JUMP,
    JUMP_IF_FALSE,
    PRINT,
}
