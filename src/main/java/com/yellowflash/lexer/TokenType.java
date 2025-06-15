package com.yellowflash.lexer;

public enum TokenType {
    // Arithmetic operators
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    REMAINDER,

    // Logical operators
    AND,
    OR,
    NOT,

    // Comparison and assignment
    NOT_EQUAL,
    ASSIGN,
    EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,

    // Punctuation
    COMMA,
    SEMICOLON,

    // Grouping symbols
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,

    // Keywords
    FUNCTION,
    FOR,
    WHILE,
    BREAK,
    CONTINUE,
    IF,
    ELSE,
    RETURN,
    PRINT,

    // Literals and identifiers
    STRING,
    FLOAT,
    BOOLEAN,
    IDENTIFIER,
    LITERAL,

    // Special
    EOF
}