package com.kaori.ast.expression;

import com.kaori.interpreter.Interpreter;

public abstract class Expression {
    public abstract Object acceptVisitor(Interpreter interpreter);
}
