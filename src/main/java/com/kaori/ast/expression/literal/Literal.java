package com.kaori.ast.expression.operators.literal;

import com.kaori.ast.expression.Expression;

public abstract class Literal extends Expression {
    public final Object value;

    public Literal(Object value) {
        this.value = value;
    }
}
