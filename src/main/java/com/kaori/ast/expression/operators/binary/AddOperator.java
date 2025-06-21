package com.kaori.ast.expression.operators.binary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Visitor;

public class AddOperator extends BinaryOperator {
    public AddOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitAddOperator(this);
    }
}
