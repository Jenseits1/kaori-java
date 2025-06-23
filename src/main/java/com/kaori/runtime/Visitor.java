package com.kaori.runtime;

import com.kaori.ast.expression.Expression;
import com.kaori.ast.statement.BlockStatement;
import com.kaori.ast.statement.ExpressionStatement;
import com.kaori.ast.statement.PrintStatement;

public interface Visitor {
    void visitExpressionStatement(ExpressionStatement node);

    void visitPrintStatement(PrintStatement node);

    void visitBlockStatement(BlockStatement node);

    Object visitLiteral(Expression.Literal node);

    Object visitAdd(Expression.Add node);

    Object visitSubtract(Expression.Subtract node);

    Object visitMultiply(Expression.Multiply node);

    Object visitDivide(Expression.Divide node);

    Object visitModulo(Expression.Modulo node);

    Object visitAnd(Expression.And node);

    Object visitOr(Expression.Or node);

    Object visitEqual(Expression.Equal node);

    Object visitNotEqual(Expression.NotEqual node);

    Object visitGreater(Expression.Greater node);

    Object visitGreaterEqual(Expression.GreaterEqual node);

    Object visitLess(Expression.Less node);

    Object visitLessEqual(Expression.LessEqual node);

    Object visitNegation(Expression.Negation node);

    Object visitNot(Expression.Not node);

}
