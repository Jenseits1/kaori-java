package com.kaori.interpreter;

import java.util.List;

import com.kaori.ast.expression.Expression;
import com.kaori.ast.expression.operators.binary.AddOperator;
import com.kaori.ast.expression.operators.binary.DivideOperator;
import com.kaori.ast.expression.operators.binary.ModuloOperator;
import com.kaori.ast.expression.operators.binary.MultiplyOperator;
import com.kaori.ast.expression.operators.binary.SubtractOperator;
import com.kaori.ast.expression.operators.literal.FloatLiteral;
import com.kaori.ast.expression.operators.literal.StringLiteral;
import com.kaori.ast.expression.operators.unary.NegationOperator;
import com.kaori.ast.statement.PrintStatement;
import com.kaori.ast.statement.Statement;

public class Interpreter {
    List<Statement> statements;

    public Interpreter(List<Statement> statements) {
        this.statements = statements;
    }

    public void run() {
        for (Statement statement : statements) {
            statement.acceptVisitor(this);
        }
    }

    public void visitPrintStatement(PrintStatement statement) {
        Object expression = statement.expression.acceptVisitor(this);

        System.out.println(expression);
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

        if (left instanceof Float && right instanceof Float) {
            return (Float) left + (Float) right;
        }

        throw new RuntimeException("operands must be numbers for '+'");
    }

    public Object visitSubtractOperator(SubtractOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Float && right instanceof Float) {
            return (Float) left - (Float) right;
        }

        throw new RuntimeException("Operands must be numbers for '-'");
    }

    public Object visitMultiplyOperator(MultiplyOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Float && right instanceof Float) {
            return (Float) left * (Float) right;
        }

        throw new RuntimeException("Operands must be numbers for '*'");
    }

    public Object visitDivideOperator(DivideOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Float && right instanceof Float) {
            if ((Float) right == 0.0) {
                throw new ArithmeticException("Division by zero");
            }
            return (Float) left / (Float) right;
        }

        throw new RuntimeException("Operands must be numbers for '/'");
    }

    public Object visitModuloOperator(ModuloOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Float && right instanceof Float) {
            return (Float) left % (Float) right;
        }

        throw new RuntimeException("Operands must be numbers for '%'");
    }

    public Object visitNegationOperator(NegationOperator operator) {
        Object operand = operator.operand.acceptVisitor(this);

        if (operand instanceof Float) {
            return -(Float) operand;
        }

        throw new RuntimeException("Operands must be numbers for '%'");
    }
}
