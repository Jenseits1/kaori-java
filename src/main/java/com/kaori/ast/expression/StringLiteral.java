package com.kaori.ast.expression;

import com.kaori.interpreter.Interpreter;

public class StringLiteral extends Expression {
    String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    public Object acceptVisitor(Interpreter interpreter) {
        return 1;
    }
}