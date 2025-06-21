package com.kaori.interpreter;

import java.util.List;

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
    public Object visitAdd(Add node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l + r;
        }

        if (left instanceof String l && right instanceof String r) {
            return l + r;
        }

        throw new RuntimeException("Operands for '+' must be numbers");
    }

    @Override
    public Object visitSubtract(Subtract node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l - r;
        }

        throw new RuntimeException("Operands for '-' must be numbers");
    }

    @Override
    public Object visitMultiply(Multiply node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l * r;
        }

        throw new RuntimeException("Operands for '*' must be numbers");
    }

    @Override
    public Object visitDivide(Divide node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            if (r == 0.0f) {
                throw new ArithmeticException("Division by zero");
            }

            return l / r;
        }

        throw new RuntimeException("Operands for '/' must be numbers");
    }

    @Override
    public Object visitModulo(Modulo node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l % r;
        }

        throw new RuntimeException("Operands for '%' must be numbers");
    }

    @Override
    public Object visitNegation(Negation node) {
        Object left = node.left.acceptVisitor(this);

        if (left instanceof Float l) {
            return -l;
        }

        throw new RuntimeException("Operand for unary '-' must be a number");
    }

    @Override
    public Object visitAnd(And node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitAnd'");
    }

    @Override
    public Object visitOr(Or node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitOr'");
    }

    @Override
    public Object visitEqual(Equal node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitEqual'");
    }

    @Override
    public Object visitNotEqual(NotEqual node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitNotEqual'");
    }

    @Override
    public Object visitGreater(Greater node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitGreater'");
    }

    @Override
    public Object visitGreaterEqual(GreaterEqual node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitGreaterEqual'");
    }

    @Override
    public Object visitLess(Less node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitLess'");
    }

    @Override
    public Object visitLessEqual(LessEqual node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitLessEqual'");
    }

    @Override
    public Object visitNot(Not node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitNot'");
    }

}
