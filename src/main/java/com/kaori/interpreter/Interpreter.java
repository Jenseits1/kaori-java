package com.kaori.interpreter;

import java.util.List;

import com.kaori.ast.expression.FloatLiteral;
import com.kaori.ast.expression.operators.binary.AddOperator;
import com.kaori.ast.expression.operators.binary.DivideOperator;
import com.kaori.ast.expression.operators.binary.ModuloOperator;
import com.kaori.ast.expression.operators.binary.MultiplyOperator;
import com.kaori.ast.expression.operators.binary.SubtractOperator;
import com.kaori.ast.expression.operators.unary.NegationOperator;
import com.kaori.ast.statement.Statement;

public class Interpreter {
    List<Statement> statements;

    public Interpreter(List<Statement> statements) {
        this.statements = statements;
    }

    public void run() {
        this.statements.get(0);
    }

    public Object visitFloatLiteral(FloatLiteral literal) {

        return 1;
    }

    public Object visitAddOperator(AddOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Double && right instanceof Double) {
            return (Double) left + (Double) right;
        }

        throw new RuntimeException("operands must be numbers for '+'");
    }

    public Object visitSubtractOperator(SubtractOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Double && right instanceof Double) {
            return (Double) left - (Double) right;
        }

        throw new RuntimeException("Operands must be numbers for '-'");
    }

    public Object visitMultiplyOperator(MultiplyOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Double && right instanceof Double) {
            return (Double) left * (Double) right;
        }

        throw new RuntimeException("Operands must be numbers for '*'");
    }

    public Object visitDivideOperator(DivideOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Double && right instanceof Double) {
            if ((Double) right == 0.0) {
                throw new ArithmeticException("Division by zero");
            }
            return (Double) left / (Double) right;
        }

        throw new RuntimeException("Operands must be numbers for '/'");
    }

    public Object visitModuloOperator(ModuloOperator operator) {
        Object left = operator.left.acceptVisitor(this);
        Object right = operator.right.acceptVisitor(this);

        if (left instanceof Double && right instanceof Double) {
            return (Double) left % (Double) right;
        }

        throw new RuntimeException("Operands must be numbers for '%'");
    }

    public Object visitNegationOperator(NegationOperator operator) {
        Object operand = operator.operand.acceptVisitor(this);

        if (operand instanceof Double) {
            return -(Double) operand;
        }

        throw new RuntimeException("Operands must be numbers for '%'");
    }
}
