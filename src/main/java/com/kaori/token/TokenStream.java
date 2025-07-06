package com.kaori.token;

import java.util.List;

import com.kaori.error.KaoriError;

public class TokenStream {
    private final List<Token> tokens;
    private String source;
    private int index;
    private Token current;
    private int line;

    public TokenStream(List<Token> tokens, String source) {
        this.tokens = tokens;
        this.source = source;
        this.index = 0;
        this.current = this.tokens.get(0);
        this.line = 1;
    }

    public TokenKind getCurrent() {
        return this.current.kind;
    }

    public int getLine() {
        return this.current.line;
    }

    public String getLexeme() {
        int start = this.current.start;
        int end = this.current.end;

        return this.source.substring(start, end);
    }

    public boolean atEnd() {
        return this.index >= this.tokens.size();
    }

    private void advance() {
        this.index++;

        if (!this.atEnd()) {
            this.current = this.tokens.get(this.index);
            this.line = this.current.line;
        }
    }

    public void consume(TokenKind expected) {
        if (current.kind != expected) {
            throw KaoriError.SyntaxError("expected '" + expected + "'", this.line);
        }

        this.advance();
    }

    public void consume() {
        this.advance();
    }

    public boolean lookAhead(TokenKind... expected) {
        for (int i = 0; i < expected.length; i++) {
            int j = this.index + i;

            if (j >= this.tokens.size()) {
                return false;
            }

            Token token = tokens.get(j);

            if (token.kind != expected[i])
                return false;
        }

        return true;
    }
}
