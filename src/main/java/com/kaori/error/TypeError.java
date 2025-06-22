package com.kaori.error;

public class TypeError extends RuntimeException {
    private final int line;

    public TypeError(String errorMessage, int line) {
        super(String.format("TypeError: %s at line %d", errorMessage, line));
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
