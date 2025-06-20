package com.kaori.ast.expression.operators.binary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Interpreter;

public class DivideOperator extends BinaryOperator {
    public DivideOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Object acceptVisitor(Interpreter interpreter) {
        return interpreter.visitDivideOperator(this);
    }
}
