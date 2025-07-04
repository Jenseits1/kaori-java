package com.kaori.token;

public class Token {
    public final TokenKind kind;
    public final int line;
    public final int start;
    public final int end;

    public Token(TokenKind kind, int line, int start, int end) {
        this.kind = kind;
        this.line = line;
        this.start = start;
        this.end = end;
    }
}
