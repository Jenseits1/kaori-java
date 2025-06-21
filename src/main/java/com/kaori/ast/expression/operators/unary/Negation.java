package com.kaori.ast.expression.operators.unary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Visitor;

public class Negation extends UnaryOperator {
    public Negation(Expression left) {
        super(left);
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitNegation(this);
    }
}
