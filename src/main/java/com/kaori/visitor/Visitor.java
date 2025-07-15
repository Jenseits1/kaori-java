package com.kaori.visitor;

import java.util.List;

import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;

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

    protected abstract void declare(ExpressionAST.Identifier identifier, T value);

    protected abstract void define(ExpressionAST.Identifier identifier, T value);

    protected abstract T get(ExpressionAST.Identifier identifier);

    protected void visitStatement(StatementAST statement) {
        if (statement instanceof StatementAST.Block block) {
            this.visitBlockStatement(block);
        } else if (statement instanceof StatementAST.ForLoop forLoop) {
            this.visitForLoopStatement(forLoop);

        } else if (statement instanceof StatementAST.Print print) {
            this.visitPrintStatement(print);

        } else if (statement instanceof StatementAST.Expr expr) {
            this.visitExpressionStatement(expr);

        } else if (statement instanceof StatementAST.Variable variable) {
            this.visitVariableStatement(variable);

        } else if (statement instanceof StatementAST.If ifStmt) {
            this.visitIfStatement(ifStmt);

        } else if (statement instanceof StatementAST.WhileLoop whileLoop) {
            this.visitWhileLoopStatement(whileLoop);

        } else if (statement instanceof StatementAST.Function function) {
            this.visitFunctionStatement(function);

        } else if (statement instanceof StatementAST.FunctionDecl funcDecl) {
            this.visitFunctionDeclStatement(funcDecl);
        }

        throw new IllegalStateException(
                "Unhandled statement type: " + statement.getClass().getSimpleName());

    }

    protected T visitExpression(ExpressionAST expression) {
        if (expression instanceof ExpressionAST.BinaryOperator binOp) {
            return this.visitBinaryOperator(binOp);
        }

        if (expression instanceof ExpressionAST.UnaryOperator unaryOp) {
            return this.visitUnaryOperator(unaryOp);
        }

        if (expression instanceof ExpressionAST.Assign assign) {
            return this.visitAssign(assign);
        }

        if (expression instanceof ExpressionAST.Literal literal) {
            return this.visitLiteral(literal);
        }

        if (expression instanceof ExpressionAST.Identifier identifier) {
            return this.visitIdentifier(identifier);
        }

        if (expression instanceof ExpressionAST.FunctionCall funcCall) {
            return this.visitFunctionCall(funcCall);
        }

        throw new IllegalStateException(
                "Unhandled expression type: " + expression.getClass().getSimpleName());
    }

    protected void visitStatements(List<StatementAST> statements) {
        for (StatementAST statement : statements) {
            this.line = statement.line;
            this.visitStatement(statement);
        }
    }

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
