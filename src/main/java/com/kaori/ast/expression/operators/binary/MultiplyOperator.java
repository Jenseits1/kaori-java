package com.kaori.ast.expression.operators;

import com.kaori.ast.expression.Expression;

public class MultiplyOperator extends BinaryOperator {
    public MultiplyOperator(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }
}
