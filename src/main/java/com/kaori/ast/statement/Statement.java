package com.kaori.ast.statement;

import com.kaori.runtime.Visitor;

public abstract class Statement {
    public final int line;

    public Statement(int line) {
        this.line = line;
    }

    public abstract void acceptVisitor(Visitor visitor);
}
