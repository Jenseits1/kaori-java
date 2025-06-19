package com.kaori.ast.expression;

import com.kaori.interpreter.Interpreter;

public class BooleanLiteral extends Expression {
    boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

     public Object acceptVisitor(Interpreter interpreter) {
        return 1;
    }
}