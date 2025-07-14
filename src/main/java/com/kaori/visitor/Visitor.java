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

    protected void visitStatements(List<StatementAST> statements) {
        for (StatementAST statement : statements) {
            this.line = statement.line;
            statement.acceptVisitor(this);
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

}
