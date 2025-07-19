package com.kaori.visitor;

import java.util.List;

import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.error.KaoriError;
import com.kaori.memory.Environment;

public class Resolver extends Visitor<Resolver.ResolutionStatus> {
    public final Environment<ResolutionStatus> environment;

    public Resolver(List<StatementAST> statements) {
        super(statements);

        this.environment = new Environment<>();
    }

    public static enum ResolutionStatus {
        UNDECLARED,
        DECLARED,
        DEFINED
    }

    private ResolutionStatus getStatus(int distance) {
        return distance == 0 ? ResolutionStatus.UNDECLARED : this.environment.get(distance);
    }

    @Override
    public ResolutionStatus visitBinaryExpression(ExpressionAST.BinaryExpression expression) {
        this.visit(expression.left());
        this.visit(expression.right());

        return ResolutionStatus.DEFINED;
    }

    @Override
    public ResolutionStatus visitUnaryExpression(ExpressionAST.UnaryExpression expression) {
        this.visit(expression.left());

        return ResolutionStatus.DEFINED;
    }

    @Override
    public ResolutionStatus visitAssign(ExpressionAST.Assign expression) {
        this.visit(expression.left());
        this.visit(expression.right());

        return ResolutionStatus.DEFINED;
    }

    @Override
    public ResolutionStatus visitLiteral(ExpressionAST.Literal expression) {
        return ResolutionStatus.DEFINED;
    }

    @Override
    public ResolutionStatus visitIdentifier(ExpressionAST.Identifier expression) {
        int distance = this.environment.search(expression.name());
        ResolutionStatus status = this.getStatus(distance);

        if (status == ResolutionStatus.UNDECLARED) {
            throw KaoriError.ResolveError(expression.name() + " is not declared", this.line);
        }
        expression.setDistance(distance);

        return status;
    }

    @Override
    public ResolutionStatus visitFunctionCall(ExpressionAST.FunctionCall expression) {
        ResolutionStatus status = this.visit(expression.callee());

        for (ExpressionAST argument : expression.arguments()) {
            this.visit(argument);
        }

        return status;
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

        int distance = this.environment.searchInner(identifier.name());
        ResolutionStatus status = this.getStatus(distance);

        switch (status) {
            case DECLARED, DEFINED ->
                throw KaoriError.ResolveError(identifier.name() + " is already declared", this.line);
            case UNDECLARED -> this.environment.define(identifier.name(), ResolutionStatus.DECLARED, distance);
        }

        identifier.setDistance(distance);
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

        int distance = this.environment.searchInner(identifier.name());
        ResolutionStatus status = this.getStatus(distance);

        switch (status) {
            case UNDECLARED -> this.environment.define(identifier.name(), ResolutionStatus.DECLARED, distance);
            case DEFINED -> throw KaoriError.ResolveError(identifier.name() + " is already defined", this.line);
            case DECLARED -> {
                if (statement.block() == null) {
                    throw KaoriError.ResolveError(identifier.name() + " is already declared", this.line);
                }
            }
        }

        identifier.setDistance(distance);

        if (statement.block() == null) {
            return;
        }

        this.environment.define(identifier.name(), ResolutionStatus.DEFINED, distance);

        this.environment.enterScope();

        for (StatementAST.Variable parameter : statement.parameters()) {
            this.visit(parameter);
        }

        List<StatementAST> statements = statement.block().statements();

        this.visitStatements(statements);

        this.environment.exitScope();
    }

}
