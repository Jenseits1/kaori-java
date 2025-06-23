package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;
import com.kaori.runtime.Visitor;

public class ExpressionStatement extends Statement {
    public final Expression expression;

    public ExpressionStatement(Expression expression, int line) {
        super(line);
        this.expression = expression;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitExpressionStatement(this);
    }
}
