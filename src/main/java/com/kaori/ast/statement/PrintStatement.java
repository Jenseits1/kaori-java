package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Visitor;

public class PrintStatement extends Statement {
    public final Expression expression;

    public PrintStatement(Expression expression, int line) {
        super(line);
        this.expression = expression;
    }

    public void acceptVisitor(Visitor visitor) {
        visitor.visitPrintStatement(this);
    }
}
