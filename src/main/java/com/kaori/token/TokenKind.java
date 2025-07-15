package com.kaori.token;

public enum TokenKind {
    // Arithmetic operators
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),

    // Unary arithmetic operators
    INCREMENT("++"),
    DECREMENT("--"),

    // Logical operators
    AND("&&"),
    OR("||"),
    NOT("!"),

    // Comparison
    NOT_EQUAL("!="),
    EQUAL("=="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    LESS("<"),
    LESS_EQUAL("<="),

    // Others
    ASSIGN("="),
    COMMA(","),
    SEMICOLON(";"),
    COLON(":"),
    DOLLAR("$"),

    // Grouping symbols
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),

    // Keywords
    FOR("for"),
    WHILE("while"),
    BREAK("break"),
    CONTINUE("continue"),
    IF("if"),
    ELSE("else"),
    RETURN("return"),
    PRINT("print"),
    FUNCTION("def"),

    // Literals and identifiers
    IDENTIFIER("identifier"),
    STRING_LITERAL("string"),
    NUMBER_LITERAL("number"),
    BOOLEAN_LITERAL("boolean"),

    // Invalid token
    INVALID("invalid"),

    // End of File
    EOF("end of file");

    public final String label;

    private TokenKind(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }
}
