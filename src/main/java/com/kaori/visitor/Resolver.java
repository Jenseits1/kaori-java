package com.kaori.visitor;

import java.util.List;

import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.error.KaoriError;
import com.kaori.memory.Environment;

public class Resolver extends Visitor<Object> {
    public final Environment<Object> environment;

    public Resolver(List<StatementAST> statements) {
        super(statements);

        this.environment = new Environment<>();
    }

    @Override
    protected void declare(ExpressionAST.Identifier identifier, Object value) {

    }

    @Override
    protected void define(ExpressionAST.Identifier identifier, Object value) {

    }

    @Override
    protected Object get(ExpressionAST.Identifier identifier) {
        int reference = this.environment.getReference(identifier.name(), this.line);

        return reference;
    }

    @Override
    public Object visitBinaryOperator(ExpressionAST.BinaryOperator expression) {
        this.visit(expression.left());
        this.visit(expression.right());

        return null;
    }

    @Override
    public Object visitUnaryOperator(ExpressionAST.UnaryOperator expression) {
        this.visit(expression.left());

        return null;
    }

    @Override
    public Object visitAssign(ExpressionAST.Assign expression) {
        this.visit((ExpressionAST) expression.left());

        this.visit(expression.right());

        return null;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal expression) {
        return null;
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier expression) {
        return this.get(expression);
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall expression) {
        this.visit(expression.callee());

        for (ExpressionAST argument : expression.arguments()) {
            this.visit(argument);
        }

        return null;
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        this.visit(statement.expression());
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.environment.enterScope();
        this.visitStatements(statement.statements());
        this.environment.exitScope();
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        ExpressionAST.Identifier identifier = statement.left();

        this.visit(statement.right());

        this.declare(identifier, null);

    }

    @Override
    public void visitExpressionStatement(StatementAST.Expr statement) {
        this.visit(statement.expression());
    }

    @Override
    public void visitIfStatement(StatementAST.If statement) {
        this.visit(statement.condition());
        this.visit(statement.thenBranch());
        this.visit(statement.elseBranch());
    }

    @Override
    public void visitWhileLoopStatement(StatementAST.WhileLoop statement) {
        this.visit(statement.condition());
        this.visit(statement.block());
    }

    @Override
    public void visitForLoopStatement(StatementAST.ForLoop statement) {
        this.visit(statement.variable());
        this.visit(statement.condition());
        this.visit(statement.block());
        this.visit(statement.increment());
    }

    @Override
    public void visitFunctionStatement(StatementAST.Function statement) {
        ExpressionAST.Identifier identifier = statement.name();

        this.environment.enterScope();

        for (StatementAST.Variable parameter : statement.parameters()) {
            this.visit(parameter);
        }

        List<StatementAST> statements = statement.block().statements();

        this.visitStatements(statements);

        this.environment.exitScope();
    }

    @Override
    public void visitFunctionDeclStatement(StatementAST.FunctionDecl statement) {
        ExpressionAST.Identifier identifier = statement.name();

        this.declare(identifier, Object.DECLARED);
    }
}
