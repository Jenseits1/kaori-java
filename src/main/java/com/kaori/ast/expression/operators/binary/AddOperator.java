package com.kaori.ast.expression.operators;

import com.kaori.ast.expression.Expression;

public class AddOperator extends BinaryOperator {
    public AddOperator(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }
}
