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

    @Override
    public ResolutionStatus visitBinaryOperator(ExpressionAST.BinaryOperator expression) {
        this.visit(expression.left());
        this.visit(expression.right());

        return ResolutionStatus.DEFINED;
    }

    @Override
    public ResolutionStatus visitUnaryOperator(ExpressionAST.UnaryOperator expression) {
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
        int reference = this.environment.search(expression.name());
        ResolutionStatus status = reference < 0 ? ResolutionStatus.UNDECLARED : this.environment.get(reference);

        if (status == ResolutionStatus.UNDECLARED) {
            throw KaoriError.ResolveError(expression.name() + " is not declared", this.line);
        }
        expression.setReference(reference);

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

        int reference = this.environment.searchInner(identifier.name());
        ResolutionStatus status = reference < 0 ? ResolutionStatus.UNDECLARED : this.environment.get(reference);

        if (status == ResolutionStatus.DECLARED || status == ResolutionStatus.DEFINED) {
            throw KaoriError.ResolveError(identifier.name() + " is already declared", this.line);
        }

        this.environment.declare(identifier.name(), ResolutionStatus.DECLARED);

        this.visit(identifier);
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

        int reference = this.environment.searchInner(identifier.name());
        ResolutionStatus status = reference < 0 ? ResolutionStatus.UNDECLARED : this.environment.get(reference);

        if (status == ResolutionStatus.UNDECLARED) {
            this.environment.declare(identifier.name(), ResolutionStatus.DECLARED);
            reference = this.environment.searchInner(identifier.name());
        } else if (status == ResolutionStatus.DEFINED) {
            throw KaoriError.ResolveError(identifier.name() + " is already defined", this.line);
        } else if (status == ResolutionStatus.DECLARED && statement.block() == null) {
            throw KaoriError.ResolveError(identifier.name() + " is already declared", this.line);
        }

        this.visit(identifier);

        if (statement.block() == null) {
            return;
        }

        this.environment.define(identifier.name(), ResolutionStatus.DEFINED, reference);

        this.environment.enterScope();

        for (StatementAST.Variable parameter : statement.parameters()) {
            this.visit(parameter);
        }

        List<StatementAST> statements = statement.block().statements();

        this.visitStatements(statements);

        this.environment.exitScope();
    }

}
