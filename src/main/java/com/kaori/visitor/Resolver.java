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
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitGreater(Expression.Greater node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitGreaterEqual(Expression.GreaterEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitLess(Expression.Less node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitLessEqual(Expression.LessEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitAssign(Expression.Assign node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitLiteral(Expression.Literal node) {
        return null;
    }

    @Override
    public Object visitIdentifier(Expression.Identifier node) {
        Environment<Object> env = this.environment.find(node);

        if (env.get(node) == null) {
            throw KaoriError.VariableError("expected " + node.value + " to be declared", this.line);
        }

        return null;
    }

    @Override
    public Object visitNot(Expression.Not node) {
        node.left.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitNegation(Expression.Negation node) {
        node.left.acceptVisitor(this);

        return null;
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
        if (this.environment.get(statement.left) == null) {
            statement.right.acceptVisitor(this);
            this.environment.set(statement.left, 1);
        } else {
            throw KaoriError.VariableError(statement.left.value + " is already declared", this.line);
        }
    }

    @Override
    public void visitExpressionStatement(Statement.Expr statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitIfStatement(Statement.If statement) {
        statement.condition.acceptVisitor(this);
        statement.thenBranch.acceptVisitor(this);
        statement.elseBranch.acceptVisitor(this);

    }

    @Override
    public void visitWhileLoopStatement(Statement.WhileLoop statement) {
        statement.condition.acceptVisitor(this);
        statement.block.acceptVisitor(this);
    }

    @Override
    public void visitForLoopStatement(Statement.ForLoop statement) {
        statement.variable.acceptVisitor(this);
        statement.condition.acceptVisitor(this);
        statement.block.acceptVisitor(this);
        statement.increment.acceptVisitor(this);

    }

}
