package com.kaori.ast.expression.operators.binary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Visitor;

public class DivideOperator extends BinaryOperator {
    public DivideOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitDivideOperator(this);
    }
}
