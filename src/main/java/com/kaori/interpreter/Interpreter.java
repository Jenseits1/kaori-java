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
import com.kaori.error.TypeError;

public class Interpreter implements Visitor {
    private final List<Statement> statements;
    private int line;

    public Interpreter(List<Statement> statements) {
        this.statements = statements;
    }

    public void run() {
        for (Statement statement : statements) {
            line = statement.line;
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

        throw new TypeError("expected number or string operands for '+'", line);
    }

    @Override
    public Object visitSubtract(Subtract node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l - r;
        }

        throw new TypeError("expected float operands for '-'", line);
    }

    @Override
    public Object visitMultiply(Multiply node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l * r;
        }

        throw new TypeError("expected float operands for '*'", line);
    }

    @Override
    public Object visitDivide(Divide node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            if (r == 0.0f) {
                throw new TypeError("division by zero", line);
            }
            return l / r;
        }

        throw new TypeError("expected float operands for '/'", line);
    }

    @Override
    public Object visitModulo(Modulo node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l % r;
        }

        throw new TypeError("expected float operands for '%'", line);
    }

    @Override
    public Object visitAnd(And node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Boolean l && right instanceof Boolean r) {
            return l && r;
        }

        throw new TypeError("expected boolean operands for '&&'", line);
    }

    @Override
    public Object visitOr(Or node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Boolean l && right instanceof Boolean r) {
            return l || r;
        }

        throw new TypeError("expected boolean operands for '||'", line);
    }

    @Override
    public Object visitEqual(Equal node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left != null && left.getClass() != right.getClass()) {
            throw new TypeError("expected operands of same type for '=='", line);
        }

        return (left == null) ? right == null : left.equals(right);
    }

    @Override
    public Object visitNotEqual(NotEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left != null && left.getClass() != right.getClass()) {
            throw new TypeError("expected operands of same type for '!='", line);
        }

        return (left == null) ? right != null : !left.equals(right);
    }

    @Override
    public Object visitGreater(Greater node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l > r;
        }

        throw new TypeError("expected float operands for '>'", line);
    }

    @Override
    public Object visitGreaterEqual(GreaterEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l >= r;
        }

        throw new TypeError("expected float operands for '>='", line);
    }

    @Override
    public Object visitLess(Less node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l < r;
        }

        throw new TypeError("expected float operands for '<'", line);
    }

    @Override
    public Object visitLessEqual(LessEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l <= r;
        }

        throw new TypeError("expected float operands for '<='", line);
    }

    @Override
    public Object visitNot(Not node) {
        Object value = node.left.acceptVisitor(this);

        if (value instanceof Boolean b) {
            return !b;
        }

        throw new TypeError("expected boolean operand for '!'", line);
    }

    @Override
    public Object visitNegation(Negation node) {
        Object left = node.left.acceptVisitor(this);

        if (left instanceof Float l) {
            return -l;
        }

        throw new TypeError("expected float operand for unary '-'", line);
    }

}
