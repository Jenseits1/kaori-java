package com.yellowflash.ast.statement;

import com.yellowflash.ast.expression.Expression;

public class ExpressionStatement extends Statement {
    Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }
}
