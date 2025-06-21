package com.kaori.ast.expression.operators.unary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Visitor;

public class NegationOperator extends UnaryOperator {
    public NegationOperator(Expression operand) {
        super(operand);
    }

    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitNegationOperator(this);
    }
}
