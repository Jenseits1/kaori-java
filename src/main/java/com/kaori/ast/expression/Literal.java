package com.kaori.ast.expression;

import com.kaori.interpreter.Visitor;

public class Literal implements Expression {
    public final Object value;

    public Literal(Object value) {
        this.value = value;
    }

    @Override
    public Object acceptVisitor(Visitor visitor) {
        return visitor.visitLiteral(this);
    }
}
