package com.kaori.ast.expression.operators;

import com.kaori.ast.expression.Expression;

public abstract class BinaryOperator extends Expression {
    protected final Expression leftOperand;
    protected final Expression rightOperand;

    public BinaryOperator(Expression leftOperand, Expression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }
}
