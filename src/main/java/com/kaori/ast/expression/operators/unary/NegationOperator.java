package com.kaori.ast.expression.operators.unary;

import com.kaori.ast.expression.Expression;

public class NegationOperator extends UnaryOperator {
    public NegationOperator(Expression operand) {
        super(operand);
    }
}
