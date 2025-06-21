package com.kaori.ast.expression.operators.unary;

import com.kaori.ast.expression.Expression;

public abstract class UnaryOperator implements Expression {
    public final Expression left;

    public UnaryOperator(Expression left) {
        this.left = left;
    }
}
