package com.kaori.ast.expression.literal;

import com.kaori.interpreter.Visitor;

public class FloatLiteral extends Literal {
    public FloatLiteral(float value) {
        super(value);
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitFloatLiteral(this);
    }

}