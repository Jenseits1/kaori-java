package com.kaori.ast.expression.operators.literal;

import com.kaori.interpreter.Interpreter;

public class StringLiteral extends Literal {
    public StringLiteral(String value) {
        super(value);
    }

    public Object acceptVisitor(Interpreter interpreter) {
        return interpreter.visitStringLiteral(this);
    }
}