package com.kaori.ast.expression.operators.binary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Interpreter;

public class SubtractOperator extends BinaryOperator {
    public SubtractOperator(Expression left, Expression right) {
        super(left, right);
    }

    public Object acceptVisitor(Interpreter interpreter) {
        return interpreter.visitSubtractOperator(this);
    }
}
