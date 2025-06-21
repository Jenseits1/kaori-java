package com.kaori.ast.expression.operators.unary;

import com.kaori.ast.expression.Expression;

public abstract class UnaryOperator implements Expression {
    public final Expression operand;

    public UnaryOperator(Expression operand) {
        this.operand = operand;
    }
}
