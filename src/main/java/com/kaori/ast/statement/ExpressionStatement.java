package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;

public class ExpressionStatement extends Statement {
    Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }
}
