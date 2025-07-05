package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.Expression;
import com.kaori.parser.Statement;

public class Resolver extends Visitor<Object> {
    public Resolver(List<Statement> statements) {
        super(statements, new Environment<Object>());
    }

    @Override
    public Object visitAdd(Expression.Add node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitSubtract(Expression.Subtract node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitMultiply(Expression.Multiply node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitDivide(Expression.Divide node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitModulo(Expression.Modulo node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitAnd(Expression.And node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitOr(Expression.Or node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitEqual(Expression.Equal node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitNotEqual(Expression.NotEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left == right) {
            return Object.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected operands of same type for '!='", this.line);
    }

    @Override
    public Object visitGreater(Expression.Greater node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left == Object.Primitive.NUMBER && right == Object.Primitive.NUMBER) {
            return Object.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>'", this.line);
    }

    @Override
    public Object visitGreaterEqual(Expression.GreaterEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left == Object.Primitive.NUMBER && right == Object.Primitive.NUMBER) {
            return Object.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>='", this.line);
    }

    @Override
    public Object visitLess(Expression.Less node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left == Object.Primitive.NUMBER && right == Object.Primitive.NUMBER) {
            return Object.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<'", this.line);
    }

    @Override
    public Object visitLessEqual(Expression.LessEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left == Object.Primitive.NUMBER && right == Object.Primitive.NUMBER) {
            return Object.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<='", this.line);
    }

    @Override
    public Object visitAssign(Expression.Assign node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left == right) {
            return right;
        }

        throw KaoriError.TypeError("expected the same type on variable assignment", this.line);
    }

    @Override
    public Object visitLiteral(Expression.Literal node) {
        return node.type;
    }

    @Override
    public Object visitIdentifier(Expression.Identifier node) {
        Object value = environment.get(node.value, this.line);

        return value;
    }

    @Override
    public Object visitNot(Expression.Not node) {
        Object value = node.left.acceptVisitor(this);

        if (value == Object.Primitive.BOOLEAN) {
            return Object.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operand for '!'", this.line);
    }

    @Override
    public Object visitNegation(Expression.Negation node) {
        Object left = node.left.acceptVisitor(this);

        if (left == Object.Primitive.NUMBER) {
            return Object.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected float operand for unary '-'", this.line);
    }

    @Override
    public void visitPrintStatement(Statement.Print statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitBlockStatement(Statement.Block statement) {
        this.environment = new Environment<Object>(environment);

        this.visitStatements(statement.statements);

        this.environment = environment.getPrevious();
    }

    @Override
    public void visitVariableStatement(Statement.Variable statement) {
        Expression.Identifier identifier = (Expression.Identifier) statement.left;

        Object left = statement.type;
        Object right = statement.right.acceptVisitor(this);

        if (left == right) {
            this.environment.declare(identifier.value, right, this.line);
        } else {
            throw KaoriError.TypeError("expected correct type in variable declaration", this.line);
        }
    }

    @Override
    public void visitExpressionStatement(Statement.Expr statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitIfStatement(Statement.If statement) {
        Object condition = statement.condition.acceptVisitor(this);

        if (condition != Object.Primitive.BOOLEAN) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.thenBranch.acceptVisitor(this);
        statement.elseBranch.acceptVisitor(this);

    }

    @Override
    public void visitWhileLoopStatement(Statement.WhileLoop statement) {
        Object condition = statement.condition.acceptVisitor(this);

        if (condition != Object.Primitive.BOOLEAN) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.block.acceptVisitor(this);
    }

    @Override
    public void visitForLoopStatement(Statement.ForLoop statement) {
        statement.variable.acceptVisitor(this);

        Object condition = statement.condition.acceptVisitor(this);

        if (condition != Object.Primitive.BOOLEAN) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.block.acceptVisitor(this);
        statement.increment.acceptVisitor(this);

    }

}
