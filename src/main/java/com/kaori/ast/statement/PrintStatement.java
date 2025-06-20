package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Interpreter;

public class PrintStatement extends Statement {
    public final Expression expression;

    public PrintStatement(Expression expression, int line) {
        super(line);
        this.expression = expression;
    }

    public void acceptVisitor(Interpreter interpreter) {
        interpreter.visitPrintStatement(this);
    }
}
