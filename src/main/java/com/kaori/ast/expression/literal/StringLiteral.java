package com.kaori.ast.expression.literal;

import com.kaori.interpreter.Visitor;

public class StringLiteral extends Literal {
    public StringLiteral(String value) {
        super(value);
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitStringLiteral(this);
    }
}