package com.kaori.lexer;

public class Token {
    public final TokenKind type;
    private final int line;
    private final int start;
    private final int end;

    public Token(TokenKind type, int line, int start, int end) {
        this.type = type;
        this.line = line;
        this.start = start;
        this.end = end;
    }

    public String lexeme(String source) {
        return source.substring(start, end);
    }

    public int getLine() {
        return this.line;
    }
}
