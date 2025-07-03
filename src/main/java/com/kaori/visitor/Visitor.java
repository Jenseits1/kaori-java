package com.kaori.visitor;

import java.util.List;

import com.kaori.parser.Expression;
import com.kaori.parser.Statement;

public abstract class Visitor<T> {
    protected int line;
    protected final List<Statement> statements;
    protected Environment environment;

    public Visitor(List<Statement> statements, Environment environment) {
        this.line = 1;
        this.statements = statements;
        this.environment = environment;
    }

    public void run() {
        visitStatements(this.statements);
    }

    protected void visitStatements(List<Statement> statements) {
        for (Statement statement : statements) {
            this.line = statement.getLine();
            statement.acceptVisitor(this);
        }
    }

    // Expressions

    public abstract T visitAdd(Expression.Add node);

    public abstract T visitSubtract(Expression.Subtract node);

    public abstract T visitMultiply(Expression.Multiply node);

    public abstract T visitDivide(Expression.Divide node);

    public abstract T visitModulo(Expression.Modulo node);

    public abstract T visitAnd(Expression.And node);

    public abstract T visitOr(Expression.Or node);

    public abstract T visitEqual(Expression.Equal node);

    public abstract T visitNotEqual(Expression.NotEqual node);

    public abstract T visitGreater(Expression.Greater node);

    public abstract T visitGreaterEqual(Expression.GreaterEqual node);

    public abstract T visitLess(Expression.Less node);

    public abstract T visitLessEqual(Expression.LessEqual node);

    public abstract T visitAssign(Expression.Assign node);

    public abstract T visitNegation(Expression.Negation node);

    public abstract T visitNot(Expression.Not node);

    public abstract T visitLiteral(Expression.Literal Literal);

    public abstract T visitIdentifier(Expression.Identifier node);

    public abstract T visitFunctionLiteral(Expression.FunctionLiteral functionLiteral);

    // Statements
    public abstract void visitExpressionStatement(Statement.Expr statement);

    public abstract void visitPrintStatement(Statement.Print statement);

    public abstract void visitVariableStatement(Statement.Variable variable);

    public abstract void visitBlockStatement(Statement.Block statement);

    public abstract void visitIfStatement(Statement.If statement);

    public abstract void visitWhileLoopStatement(Statement.WhileLoop statement);

    public abstract void visitForLoopStatement(Statement.ForLoop statement);

}
