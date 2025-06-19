package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;

public class PrintStatement extends Statement {
    Expression expression;
    int line;

    public PrintStatement(Expression expression, int line) {
        this.expression = expression;
        this.line = line;
    }
}
