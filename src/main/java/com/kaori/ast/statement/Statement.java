package com.kaori.ast.statement;

import com.kaori.interpreter.Interpreter;

public abstract class Statement {
    final int line;

    public Statement(int line) {
        this.line = line;
    }

    public abstract void acceptVisitor(Interpreter interpreter);
}
