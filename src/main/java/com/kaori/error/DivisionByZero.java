package com.kaori.error;

public class DivisionByZero extends RuntimeException {
    private final int line;

    public DivisionByZero(String errorMessage, int line) {
        super(String.format("DivisionByZero: %s at line %d", errorMessage, line));
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
