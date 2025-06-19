package com.kaori.lexer;

public class Token {
    public final TokenType type;
    public final int line;
    public final String lexeme;

    public Token(TokenType type, int line, String lexeme) {
        this.type = type;
        this.line = line;
        this.lexeme = lexeme;
    }
}
