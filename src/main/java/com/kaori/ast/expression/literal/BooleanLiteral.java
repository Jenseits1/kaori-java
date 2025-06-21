package com.kaori.ast.expression.literal;

import com.kaori.interpreter.Visitor;

public class BooleanLiteral extends Literal {
    public BooleanLiteral(boolean value) {
        super(value);
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitBooleanLiteral(this);
    }
}