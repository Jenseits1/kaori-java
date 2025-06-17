package com.yellowflash.lexer;

public class Token {
    public TokenType type;
    public int line;
    public String lexeme;

    public Token(TokenType type, int line, String lexeme) {
        this.type = type;
        this.line = line;
        this.lexeme = lexeme;
    }
}
