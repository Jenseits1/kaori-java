package com.kaori.interpreter;

import com.kaori.ast.expression.Literal;
import com.kaori.ast.expression.operators.binary.Add;
import com.kaori.ast.expression.operators.binary.And;
import com.kaori.ast.expression.operators.binary.Divide;
import com.kaori.ast.expression.operators.binary.Equal;
import com.kaori.ast.expression.operators.binary.Greater;
import com.kaori.ast.expression.operators.binary.GreaterEqual;
import com.kaori.ast.expression.operators.binary.Less;
import com.kaori.ast.expression.operators.binary.LessEqual;
import com.kaori.ast.expression.operators.binary.Modulo;
import com.kaori.ast.expression.operators.binary.Multiply;
import com.kaori.ast.expression.operators.binary.NotEqual;
import com.kaori.ast.expression.operators.binary.Or;
import com.kaori.ast.expression.operators.binary.Subtract;
import com.kaori.ast.expression.operators.unary.Negation;
import com.kaori.ast.expression.operators.unary.Not;
import com.kaori.ast.statement.ExpressionStatement;
import com.kaori.ast.statement.PrintStatement;

public interface Visitor {
    void visitExpressionStatement(ExpressionStatement node);

    void visitPrintStatement(PrintStatement node);

    Object visitLiteral(Literal node);

    Object visitAdd(Add node);

    Object visitSubtract(Subtract node);

    Object visitMultiply(Multiply node);

    Object visitDivide(Divide node);

    Object visitModulo(Modulo node);

    Object visitAnd(And node);

    Object visitOr(Or node);

    Object visitEqual(Equal node);

    Object visitNotEqual(NotEqual node);

    Object visitGreater(Greater node);

    Object visitGreaterEqual(GreaterEqual node);

    Object visitLess(Less node);

    Object visitLessEqual(LessEqual node);

    Object visitNegation(Negation node);

    Object visitNot(Not node);

}
