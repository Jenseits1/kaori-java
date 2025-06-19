package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;

public class ExpressionStatement extends Statement {
    Expression expression;
    int line;

    public ExpressionStatement(Expression expression, int line) {
        this.expression = expression;
        this.line = line;
    }
}
