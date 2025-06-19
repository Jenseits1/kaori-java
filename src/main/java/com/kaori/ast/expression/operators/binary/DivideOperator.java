package com.kaori.ast.expression.operators;

import com.kaori.ast.expression.Expression;

public class DivideOperator extends BinaryOperator {
    public DivideOperator(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }
}
