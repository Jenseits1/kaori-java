package com.kaori.lexer;

public class Token {
    public final TokenKind type;
    public final int line;
    public final int start;
    public final int end;

    public Token(TokenKind type, int line, int start, int end) {
        this.type = type;
        this.line = line;
        this.start = start;
        this.end = end;
    }

    public String getSubstring(String source) {
        return source.substring(start, end);
    }
}
