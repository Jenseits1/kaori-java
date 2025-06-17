package com.yellowflash.ast;

public class StringLiteral extends Expression {
    String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}