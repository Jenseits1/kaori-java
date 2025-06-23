package com.kaori.error;

public class KaoriError extends RuntimeException {
    private final int line;

    public KaoriError(String errorMessage, int line) {
        super(errorMessage);
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public static class DivisionByZero extends KaoriError {
        public DivisionByZero(String errorMessage, int line) {
            super(String.format("DivisionByZero: %s at line %d", errorMessage, line), line);
        }
    }

    public static class SyntaxError extends KaoriError {
        public SyntaxError(String errorMessage, int line) {
            super(String.format("SyntaxError: %s at line %d", errorMessage, line), line);
        }
    }

    public static class TypeError extends KaoriError {
        public TypeError(String errorMessage, int line) {
            super(String.format("TypeError: %s at line %d", errorMessage, line), line);

        }
    }

}
