package com.kaori.visitor;

import java.util.List;

import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;

public abstract class Visitor<T> {
    protected int line;
    protected final List<StatementAST> statements;

    public Visitor(List<StatementAST> statements) {
        this.line = 1;
        this.statements = statements;

    }

    public void run() {
        this.visitStatements(this.statements);
    }

    protected void visit(StatementAST statement) {
        if (statement instanceof StatementAST.Block stmt) {
            this.visitBlockStatement(stmt);

        } else if (statement instanceof StatementAST.ForLoop stmt) {
            this.visitForLoopStatement(stmt);

        } else if (statement instanceof StatementAST.Print stmt) {
            this.visitPrintStatement(stmt);

        } else if (statement instanceof StatementAST.Expr stmt) {
            this.visitExpressionStatement(stmt);

        } else if (statement instanceof StatementAST.Variable stmt) {
            this.visitVariableStatement(stmt);

        } else if (statement instanceof StatementAST.If stmt) {
            this.visitIfStatement(stmt);

        } else if (statement instanceof StatementAST.WhileLoop stmt) {
            this.visitWhileLoopStatement(stmt);

        } else if (statement instanceof StatementAST.Function stmt) {
            this.visitFunctionStatement(stmt);

        } else if (statement instanceof StatementAST.FunctionDecl stmt) {
            this.visitFunctionDeclStatement(stmt);

        } else {
            throw new IllegalStateException(
                    "Unhandled statement type: " + statement.getClass().getSimpleName());
        }
    }

    protected T visit(ExpressionAST expression) {
        if (expression instanceof ExpressionAST.BinaryOperator expr) {
            return this.visitBinaryOperator(expr);
        }
        if (expression instanceof ExpressionAST.UnaryOperator expr) {
            return this.visitUnaryOperator(expr);
        }
        if (expression instanceof ExpressionAST.Assign expr) {
            return this.visitAssign(expr);
        }
        if (expression instanceof ExpressionAST.Literal expr) {
            return this.visitLiteral(expr);
        }
        if (expression instanceof ExpressionAST.Identifier expr) {
            return this.visitIdentifier(expr);
        }
        if (expression instanceof ExpressionAST.FunctionCall expr) {
            return this.visitFunctionCall(expr);
        }
        throw new IllegalStateException(
                "Unhandled expression type: " + expression.getClass().getSimpleName());
    }

    protected void visitStatements(List<StatementAST> statements) {
        for (StatementAST statement : statements) {
            this.line = statement.line();
            this.visit(statement);
        }
    }

    protected abstract void declare(ExpressionAST.Identifier identifier, T value);

    protected abstract void define(ExpressionAST.Identifier identifier, T value);

    protected abstract T get(ExpressionAST.Identifier identifier);

    // Expressions
    public abstract T visitBinaryOperator(ExpressionAST.BinaryOperator node);

    public abstract T visitUnaryOperator(ExpressionAST.UnaryOperator node);

    public abstract T visitAssign(ExpressionAST.Assign node);

    public abstract T visitLiteral(ExpressionAST.Literal node);

    public abstract T visitIdentifier(ExpressionAST.Identifier node);

    public abstract T visitFunctionCall(ExpressionAST.FunctionCall node);

    // Statements
    public abstract void visitExpressionStatement(StatementAST.Expr statement);

    public abstract void visitPrintStatement(StatementAST.Print statement);

    public abstract void visitVariableStatement(StatementAST.Variable statement);

    public abstract void visitBlockStatement(StatementAST.Block statement);

    public abstract void visitIfStatement(StatementAST.If statement);

    public abstract void visitWhileLoopStatement(StatementAST.WhileLoop statement);

    public abstract void visitForLoopStatement(StatementAST.ForLoop statement);

    public abstract void visitFunctionStatement(StatementAST.Function statement);

    public abstract void visitFunctionDeclStatement(StatementAST.FunctionDecl statement);

}
