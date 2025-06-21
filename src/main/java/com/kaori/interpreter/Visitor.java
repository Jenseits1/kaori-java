package com.kaori.interpreter;

import com.kaori.ast.expression.literal.BooleanLiteral;
import com.kaori.ast.expression.literal.FloatLiteral;
import com.kaori.ast.expression.literal.StringLiteral;
import com.kaori.ast.expression.operators.binary.AddOperator;
import com.kaori.ast.expression.operators.binary.DivideOperator;
import com.kaori.ast.expression.operators.binary.ModuloOperator;
import com.kaori.ast.expression.operators.binary.MultiplyOperator;
import com.kaori.ast.expression.operators.binary.SubtractOperator;
import com.kaori.ast.expression.operators.unary.NegationOperator;
import com.kaori.ast.statement.ExpressionStatement;
import com.kaori.ast.statement.PrintStatement;

public interface Visitor {
    void visitExpressionStatement(ExpressionStatement statement);

    void visitPrintStatement(PrintStatement statement);

    Object visitBooleanLiteral(BooleanLiteral literal);

    Object visitStringLiteral(StringLiteral literal);

    Object visitFloatLiteral(FloatLiteral literal);

    Object visitAddOperator(AddOperator operator);

    Object visitSubtractOperator(SubtractOperator operator);

    Object visitMultiplyOperator(MultiplyOperator operator);

    Object visitDivideOperator(DivideOperator operator);

    Object visitModuloOperator(ModuloOperator operator);

    Object visitNegationOperator(NegationOperator operator);
}
