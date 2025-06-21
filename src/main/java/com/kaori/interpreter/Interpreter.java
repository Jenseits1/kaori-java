package com.kaori.interpreter;

import java.util.List;

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
import com.kaori.ast.statement.Statement;

public class Interpreter implements Visitor {
    List<Statement> statements;

    public Interpreter(List<Statement> statements) {
        this.statements = statements;
    }

    public void run() {
        for (Statement statement : statements) {
            statement.acceptVisitor(this);
        }
    }

    public void visitExpressionStatement(ExpressionStatement statement) {
        statement.expression.acceptVisitor(this);
    }

    public void visitPrintStatement(PrintStatement statement) {
        Object expression = statement.expression.acceptVisitor(this);

        System.out.println(expression);
    }

    public Object visitBooleanLiteral(BooleanLiteral literal) {
        return literal.value;
    }

    public Object visitStringLiteral(StringLiteral literal) {
        return literal.value;
    }

    public Object visitFloatLiteral(FloatLiteral literal) {
        return literal.value;
    }

    public Object visitAddOperator(AddOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        typeCheck(left, right, "+");

        return (Float) left + (Float) right;
    }

    public Object visitSubtractOperator(SubtractOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        typeCheck(left, right, "-");

        return (Float) left - (Float) right;
    }

    public Object visitMultiplyOperator(MultiplyOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        typeCheck(left, right, "*");

        return (Float) left * (Float) right;
    }

    public Object visitDivideOperator(DivideOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        typeCheck(left, right, "/");

        if ((Float) right == 0.0) {
            throw new ArithmeticException("Division by zero");
        }

        return (Float) left / (Float) right;

    }

    public Object visitModuloOperator(ModuloOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        typeCheck(left, right, "%");

        return (Float) left % (Float) right;
    }

    public Object visitNegationOperator(NegationOperator operator) {
        Object left = operator.left.acceptVisitor(this);

        typeCheck(left, "-");
        return -(Float) left;
    }

    private void typeCheck(Object left, Object right, String operator) {
        if (left instanceof Float && right instanceof Float) {
            return;
        }

        throw new RuntimeException("expected number operands for " + operator);
    }

    private void typeCheck(Object left, String operator) {
        if (left instanceof Float) {
            return;
        }

        throw new RuntimeException("expected number operand for " + operator);
    }
}
