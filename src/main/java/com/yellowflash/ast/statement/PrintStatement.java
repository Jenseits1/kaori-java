package com.yellowflash.ast.statement;

import com.yellowflash.ast.expression.Expression;

public class PrintStatement extends Statement {
    Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }
}
