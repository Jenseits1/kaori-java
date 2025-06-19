package com.kaori.ast.expression;

public class BooleanLiteral extends Expression {
    boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }
}