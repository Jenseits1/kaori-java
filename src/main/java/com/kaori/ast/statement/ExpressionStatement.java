package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Visitor;

public class ExpressionStatement extends Statement {
    public final Expression expression;

    public ExpressionStatement(Expression expression, int line) {
        super(line);
        this.expression = expression;
    }

    public void acceptVisitor(Visitor visitor) {
        visitor.visitExpressionStatement(this);
    }
}
