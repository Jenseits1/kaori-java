package com.kaori.token;

public record Token(TokenKind kind, int line, int position, int size) {
}
