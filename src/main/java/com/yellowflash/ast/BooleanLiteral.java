package com.yellowflash.ast;

public class BooleanLiteral extends Expression {
    boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }
}