package com.kaori.lexer;

public enum TokenType {
    // Arithmetic operators
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    MODULO,

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
    STRING,
    FLOAT,
    BOOLEAN,
    // Literals and identifiers

    IDENTIFIER,
    STRING_LITERAL,
    FLOAT_LITERAL,
    BOOLEAN_LITERAL,

    // Special
    INVALID_TOKEN,
    EOF
}