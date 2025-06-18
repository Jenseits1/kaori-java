package com.yellowflash.ast.expression;

public class FloatLiteral extends Expression {
    float value;

    public FloatLiteral(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }
}