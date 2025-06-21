package com.kaori.ast.expression.operators.binary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Visitor;

public class MultiplyOperator extends BinaryOperator {
    public MultiplyOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitMultiplyOperator(this);
    }
}
