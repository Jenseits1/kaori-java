package com.kaori.ast.statement;

import com.kaori.ast.expression.Expression;
import com.kaori.runtime.Visitor;

public class VariableStatement extends Statement {
    Expression expression;

    public VariableStatement(int line, Expression expression) {
        super(line);
        this.expression = expression;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'acceptVisitor'");
    }

}
