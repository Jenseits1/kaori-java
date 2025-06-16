package com.yellowflash.lexer;

public class Token {
    TokenType type;
    int line;
    String lexeme;

    public Token(TokenType type, int line, String lexeme) {
        this.type = type;
        this.line = line;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return String.format(
                "Token {\n  type: %s,\n  line: %d,\n  lexeme: %s\n}",
                type, line, lexeme);
    }
}
