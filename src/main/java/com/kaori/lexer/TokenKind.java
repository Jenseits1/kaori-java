package com.kaori.lexer;

public enum TokenKind {
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

    // Comparison
    NOT_EQUAL,
    EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,

    // Punctuation
    ASSIGN,
    COMMA,
    SEMICOLON,
    COLON,

    // Grouping symbols
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,

    // Keywords
    FOR,
    WHILE,
    BREAK,
    CONTINUE,
    IF,
    ELSE,
    RETURN,
    PRINT,
    FUNCTION,

    // Literals and identifiers
    IDENTIFIER,
    STRING_LITERAL,
    NUMBER_LITERAL,
    BOOLEAN_LITERAL,

}