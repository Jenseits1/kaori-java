package com.kaori.interpreter;

import java.util.List;

import com.kaori.ast.expression.Literal;
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
    private final List<Statement> statements;

    public Interpreter(List<Statement> statements) {
        this.statements = statements;
    }

    public void run() {
        for (Statement statement : statements) {
            statement.acceptVisitor(this);
        }
    }

    @Override
    public void visitExpressionStatement(ExpressionStatement statement) {
        statement.expression.acceptVisitor(this);
    }

    public void visitPrintStatement(PrintStatement statement) {
        Object expression = statement.expression.acceptVisitor(this);

        System.out.println(expression);
    }

    @Override
    public Object visitLiteral(Literal literal) {
        return literal.value;
    }

    @Override
    public Object visitAddOperator(AddOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l + r;
        }

        if (left instanceof String l && right instanceof String r) {
            return l + r;
        }

        throw new RuntimeException("Operands for '+' must be numbers");
    }

    @Override
    public Object visitSubtractOperator(SubtractOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l - r;
        }

        throw new RuntimeException("Operands for '-' must be numbers");
    }

    @Override
    public Object visitMultiplyOperator(MultiplyOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l * r;
        }

        throw new RuntimeException("Operands for '*' must be numbers");
    }

    @Override
    public Object visitDivideOperator(DivideOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            if (r == 0.0f) {
                throw new ArithmeticException("Division by zero");
            }

            return l / r;
        }

        throw new RuntimeException("Operands for '/' must be numbers");
    }

    @Override
    public Object visitModuloOperator(ModuloOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l % r;
        }

        throw new RuntimeException("Operands for '%' must be numbers");
    }

    @Override
    public Object visitNegationOperator(NegationOperator operator) {
        Object left = operator.left.acceptVisitor(this);

        if (left instanceof Float l) {
            return -l;
        }

        throw new RuntimeException("Operand for unary '-' must be a number");
    }

}
