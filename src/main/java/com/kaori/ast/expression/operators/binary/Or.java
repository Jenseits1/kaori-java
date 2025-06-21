package com.kaori.ast.expression.operators.binary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Visitor;

public class Or extends BinaryOperator {
    public Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitOr(this);
    }
}
