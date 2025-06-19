package com.kaori.ast.expression.operators;

import com.kaori.ast.expression.Expression;

public class ModuloOperator extends BinaryOperator {
    public ModuloOperator(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }
}
