package com.kaori.token;

public class Token {
    public final TokenKind kind;
    public final int line;
    public final int position;
    public final int size;

    public Token(TokenKind kind, int line, int position, int size) {
        this.kind = kind;
        this.line = line;
        this.position = position;
        this.size = size;
    }
}
