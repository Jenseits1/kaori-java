package com.kaori.visitor;

import java.util.List;

import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;
import com.kaori.visitor.memory.Environment;

public abstract class Visitor<T> {
    protected int line;
    protected final List<StatementAST> statements;
    protected Environment<T> environment;

    public Visitor(List<StatementAST> statements) {
        this.line = 1;
        this.statements = statements;
        this.environment = new Environment<>();
    }

    public void run() {
        visitStatements(this.statements);
    }

    protected void visitStatements(List<StatementAST> statements) {
        for (StatementAST statement : statements) {
            this.line = statement.line;
            statement.acceptVisitor(this);
        }
    }

    // Expressions
    public abstract T visitAdd(ExpressionAST.Add node);

    public abstract T visitSubtract(ExpressionAST.Subtract node);

    public abstract T visitMultiply(ExpressionAST.Multiply node);

    public abstract T visitDivide(ExpressionAST.Divide node);

    public abstract T visitModulo(ExpressionAST.Modulo node);

    public abstract T visitAnd(ExpressionAST.And node);

    public abstract T visitOr(ExpressionAST.Or node);

    public abstract T visitEqual(ExpressionAST.Equal node);

    public abstract T visitNotEqual(ExpressionAST.NotEqual node);

    public abstract T visitGreater(ExpressionAST.Greater node);

    public abstract T visitGreaterEqual(ExpressionAST.GreaterEqual node);

    public abstract T visitLess(ExpressionAST.Less node);

    public abstract T visitLessEqual(ExpressionAST.LessEqual node);

    public abstract T visitAssign(ExpressionAST.Assign node);

    public abstract T visitNegation(ExpressionAST.Negation node);

    public abstract T visitNot(ExpressionAST.Not node);

    public abstract T visitLiteral(ExpressionAST.Literal node);

    public abstract T visitIdentifier(ExpressionAST.Identifier node);

    public abstract T visitFunctionCall(ExpressionAST.FunctionCall node);

    // Statements
    public abstract void visitExpressionStatement(StatementAST.Expr statement);

    public abstract void visitPrintStatement(StatementAST.Print statement);

    public abstract void visitVariableStatement(StatementAST.Variable variable);

    public abstract void visitBlockStatement(StatementAST.Block statement);

    public abstract void visitIfStatement(StatementAST.If statement);

    public abstract void visitWhileLoopStatement(StatementAST.WhileLoop statement);

    public abstract void visitForLoopStatement(StatementAST.ForLoop statement);

    public abstract void visitFunctionStatement(StatementAST.Function statement);
}
