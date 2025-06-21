package com.kaori.ast.expression;

import com.kaori.interpreter.Visitor;

public interface Expression {
    public Object acceptVisitor(Visitor visitor);
}
