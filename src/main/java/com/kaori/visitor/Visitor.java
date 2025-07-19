package com.kaori.visitor;

import java.util.List;

import com.kaori.ast.DeclarationAST;
import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;

public abstract class Visitor<T> {
    protected int line;
    protected final StatementAST.Block block;

    public Visitor(StatementAST.Block block) {
        this.line = 1;
        this.block = block;

    }

    public void run() {
        this.visitBlockStatement(block);
    }

    protected void visitDeclarations(List<DeclarationAST> declarations) {
        for (DeclarationAST declaration : declarations) {
            this.line = declaration.line();
            this.visit(declaration);
        }
    }

    protected void visit(DeclarationAST declaration) {
        if (declaration instanceof DeclarationAST.Variable decl) {
            this.visitVariableDeclaration(decl);
        } else if (declaration instanceof DeclarationAST.Function decl) {
            this.visitFunctionDeclaration(decl);
        } else if (declaration instanceof StatementAST stmt) {
            this.visit(stmt);
        } else {
            throw new IllegalStateException(
                    "Unhandled statement type: " + declaration.getClass().getSimpleName());
        }
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

        } else if (statement instanceof StatementAST.If stmt) {
            this.visitIfStatement(stmt);

        } else if (statement instanceof StatementAST.WhileLoop stmt) {
            this.visitWhileLoopStatement(stmt);
        } else {
            throw new IllegalStateException(
                    "Unhandled statement type: " + statement.getClass().getSimpleName());
        }
    }

    protected T visit(ExpressionAST expression) {
        if (expression instanceof ExpressionAST.BinaryExpression expr) {
            return this.visitBinaryExpression(expr);
        }
        if (expression instanceof ExpressionAST.UnaryExpression expr) {
            return this.visitUnaryExpression(expr);
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

    // Expressions
    public abstract T visitBinaryExpression(ExpressionAST.BinaryExpression expression);

    public abstract T visitUnaryExpression(ExpressionAST.UnaryExpression expression);

    public abstract T visitAssign(ExpressionAST.Assign expression);

    public abstract T visitLiteral(ExpressionAST.Literal expression);

    public abstract T visitIdentifier(ExpressionAST.Identifier expression);

    public abstract T visitFunctionCall(ExpressionAST.FunctionCall expression);

    // Statements
    public abstract void visitExpressionStatement(StatementAST.Expr statement);

    public abstract void visitPrintStatement(StatementAST.Print statement);

    public abstract void visitBlockStatement(StatementAST.Block statement);

    public abstract void visitIfStatement(StatementAST.If statement);

    public abstract void visitWhileLoopStatement(StatementAST.WhileLoop statement);

    public abstract void visitForLoopStatement(StatementAST.ForLoop statement);

    // Declarations
    public abstract void visitVariableDeclaration(DeclarationAST.Variable declaration);

    public abstract void visitFunctionDeclaration(DeclarationAST.Function declaration);

}
