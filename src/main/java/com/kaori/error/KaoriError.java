package com.kaori.error;

public class KaoriError extends RuntimeException {
    protected final int line;

    private KaoriError(String errorMessage, int line) {
        super(errorMessage);
        this.line = line;
    }

    public String toString() {
        return super.getMessage();
    }

    public static KaoriError DivisionByZero(String errorMessage, int line) {
        String formattedMessage = String.format("DivisionByZero: %s at line %d", errorMessage, line);

        return new KaoriError(formattedMessage, line);
    }

    public static KaoriError SyntaxError(String errorMessage, int line) {
        String formattedMessage = String.format("SyntaxError: %s at line %d", errorMessage, line);

        return new KaoriError(formattedMessage, line);
    }

    public static KaoriError TypeError(String errorMessage, int line) {
        String formattedMessage = String.format("TypeError: %s at line %d", errorMessage, line);

        return new KaoriError(formattedMessage, line);
    }

    public static KaoriError VariableError(String errorMessage, int line) {
        String formattedMessage = String.format("VariableError: %s at line %d", errorMessage, line);

        return new KaoriError(formattedMessage, line);
    }
}
