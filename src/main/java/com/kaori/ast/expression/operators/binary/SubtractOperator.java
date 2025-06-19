package com.kaori.ast.expression.operators;

import com.kaori.ast.expression.Expression;

public class SubtractOperator extends BinaryOperator {
    public SubtractOperator(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }
}
