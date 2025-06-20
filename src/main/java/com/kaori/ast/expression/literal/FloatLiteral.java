package com.kaori.ast.expression.operators.literal;

import com.kaori.interpreter.Interpreter;

public class FloatLiteral extends Literal {
    public FloatLiteral(float value) {
        super(value);
    }

    @Override
    public Object acceptVisitor(Interpreter interpreter) {
        return interpreter.visitFloatLiteral(this);
    }

}