package com.kaori.runtime;

import java.util.List;

import com.kaori.ast.Expression;
import com.kaori.ast.Statement;
import com.kaori.error.KaoriError;

public class Interpreter implements Expression.Visitor, Statement.Visitor {
    private final List<Statement> statements;
    private int line;

    public Interpreter(List<Statement> statements) {
        this.statements = statements;
    }

    public void run() {
        for (Statement statement : statements) {
            statement.acceptVisitor(this);
        }
    }

    public void visitPrintStatement(Statement.Print statement) {
        Object expression = statement.expression.acceptVisitor(this);

        System.out.println(expression);
    }

    @Override
    public void visitBlockStatement(Statement.Block node) {
        for (Statement statement : node.statements) {
            statement.acceptVisitor(this);
        }
    }

    @Override
    public void visitExpressionStatement(Statement.Expr statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public Object visitLiteral(Expression.Literal literal) {
        return literal.value;
    }

    @Override
    public Object visitAdd(Expression.Add node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l + r;
        }

        if (left instanceof String l && right instanceof String r) {
            return l + r;
        }

        throw KaoriError.TypeError("expected number or string operands for '+'", line);
    }

    @Override
    public Object visitSubtract(Expression.Subtract node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l - r;
        }

        throw KaoriError.TypeError("expected float operands for '-'", line);
    }

    @Override
    public Object visitMultiply(Expression.Multiply node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l * r;
        }

        throw KaoriError.TypeError("expected float operands for '*'", line);
    }

    @Override
    public Object visitDivide(Expression.Divide node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            if (r == 0.0f) {
                throw KaoriError.DivisionByZero("can not do division by zero", line);
            }

            return l / r;
        }

        throw KaoriError.TypeError("expected float operands for '/'", line);
    }

    @Override
    public Object visitModulo(Expression.Modulo node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            if (r == 0.0f) {
                throw KaoriError.DivisionByZero("can not do division by zero", line);
            }

            return l % r;
        }

        throw KaoriError.TypeError("expected float operands for '%'", line);
    }

    @Override
    public Object visitAnd(Expression.And node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Boolean l && right instanceof Boolean r) {
            return l && r;
        }

        throw KaoriError.TypeError("expected boolean operands for '&&'", line);
    }

    @Override
    public Object visitOr(Expression.Or node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Boolean l && right instanceof Boolean r) {
            return l || r;
        }

        throw KaoriError.TypeError("expected boolean operands for '||'", line);
    }

    @Override
    public Object visitEqual(Expression.Equal node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof String l && right instanceof String r) {
            return l.equals(r);
        }

        if (left instanceof Float l && right instanceof Float r) {
            return l.equals(r);
        }

        if (left instanceof Boolean l && right instanceof Boolean r) {
            return l.equals(r);
        }

        throw KaoriError.TypeError("expected operands of same type for '=='", line);
    }

    @Override
    public Object visitNotEqual(Expression.NotEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof String l && right instanceof String r) {
            return !l.equals(r);
        }

        if (left instanceof Float l && right instanceof Float r) {
            return !l.equals(r);
        }

        if (left instanceof Boolean l && right instanceof Boolean r) {
            return !l.equals(r);
        }

        throw KaoriError.TypeError("expected operands of same type for '!='", line);
    }

    @Override
    public Object visitGreater(Expression.Greater node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l > r;
        }

        throw KaoriError.TypeError("expected float operands for '>'", line);
    }

    @Override
    public Object visitGreaterEqual(Expression.GreaterEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l >= r;
        }

        throw KaoriError.TypeError("expected float operands for '>='", line);
    }

    @Override
    public Object visitLess(Expression.Less node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l < r;
        }

        throw KaoriError.TypeError("expected float operands for '<'", line);
    }

    @Override
    public Object visitLessEqual(Expression.LessEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float l && right instanceof Float r) {
            return l <= r;
        }

        throw KaoriError.TypeError("expected float operands for '<='", line);
    }

    @Override
    public Object visitNot(Expression.Not node) {
        Object value = node.left.acceptVisitor(this);

        if (value instanceof Boolean b) {
            return !b;
        }

        throw KaoriError.TypeError("expected boolean operand for '!'", line);
    }

    @Override
    public Object visitNegation(Expression.Negation node) {
        Object left = node.left.acceptVisitor(this);

        if (left instanceof Float l) {
            return -l;
        }

        throw KaoriError.TypeError("expected float operand for unary '-'", line);
    }
}
