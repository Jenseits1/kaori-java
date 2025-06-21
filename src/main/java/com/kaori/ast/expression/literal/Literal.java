package com.kaori.ast.expression.literal;

import com.kaori.ast.expression.Expression;

public abstract class Literal implements Expression {
    public final Object value;

    public Literal(Object value) {
        this.value = value;
    }
}
