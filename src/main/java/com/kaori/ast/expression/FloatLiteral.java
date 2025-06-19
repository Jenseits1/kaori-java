package com.kaori.ast.expression;

import com.kaori.interpreter.Interpreter;

public class FloatLiteral extends Expression {
    float value;

    public FloatLiteral(float value) {
        this.value = value;
    }

    public Object acceptVisitor(Interpreter interpreter) {
        return 1;
    }

}