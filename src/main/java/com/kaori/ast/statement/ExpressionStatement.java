package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Interpreter;

public class ExpressionStatement extends Statement {
    Expression expression;

    public ExpressionStatement(Expression expression, int line) {
        super(line);
        this.expression = expression;
    }

    public void acceptVisitor(Interpreter interpreter) {

    }
}
