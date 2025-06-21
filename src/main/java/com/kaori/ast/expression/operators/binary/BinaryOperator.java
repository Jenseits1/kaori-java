package com.kaori.ast.expression.operators.binary;

import com.kaori.ast.expression.Expression;

public abstract class BinaryOperator implements Expression {
    public final Expression left;
    public final Expression right;

    public BinaryOperator(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
}
