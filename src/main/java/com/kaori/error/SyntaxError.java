package com.kaori.error;

public class SyntaxError extends Exception {
    private final int line;

    public SyntaxError(String errorMessage, int line) {
        super(String.format("SyntaxError: %s at line %d", errorMessage, line));
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
