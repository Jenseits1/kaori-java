package com.kaori.ast.expression.operators.unary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Visitor;

public class Not extends UnaryOperator {
    public Not(Expression left) {
        super(left);
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitNot(this);
    }

}
