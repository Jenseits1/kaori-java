package com.kaori.ast.expression.operators.literal;

import com.kaori.interpreter.Interpreter;

public class BooleanLiteral extends Literal {
    public BooleanLiteral(boolean value) {
        super(value);
    }

    public Object acceptVisitor(Interpreter interpreter) {
        return true;
    }
}