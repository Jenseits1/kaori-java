package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;

public class PrintStatement extends Statement {
    Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }
}
