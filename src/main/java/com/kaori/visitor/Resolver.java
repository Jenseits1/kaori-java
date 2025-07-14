package com.kaori.visitor;

import java.util.ArrayList;
import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;
import com.kaori.parser.StatementAST.FunctionDecl;
import com.kaori.visitor.memory.Environment;

public class Resolver extends Visitor<Resolver.ResolverState> {
    public final Environment<ResolverState> environment;

    public Resolver(List<StatementAST> statements) {
        super(statements);

        this.environment = new Environment<>();
    }

    public static enum ResolverState {
        UNDECLARED,
        DECLARED,
        DEFINED
    }

    @Override
    public ResolverState visitBinaryOperator(ExpressionAST.BinaryOperator node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return ResolverState.DEFINED;
    }

    @Override
    public ResolverState visitUnaryOperator(ExpressionAST.UnaryOperator node) {
        node.left.acceptVisitor(this);

        return ResolverState.DEFINED;
    }

    @Override
    public ResolverState visitAssign(ExpressionAST.Assign node) {
        node.right.acceptVisitor(this);

        String identifier = node.left.value;

        if (this.environment.find(identifier)) {
            this.environment.define(identifier, ResolverState.DEFINED);
        } else {
            throw KaoriError.VariableError(identifier + " is not isDeclared", this.line);
        }

        return ResolverState.DEFINED;
    }

    @Override
    public ResolverState visitLiteral(ExpressionAST.Literal node) {
        if (node.value == null) {
            return ResolverState.DECLARED;
        }

        return ResolverState.DEFINED;
    }

    @Override
    public ResolverState visitIdentifier(ExpressionAST.Identifier node) {
        String identifier = node.value;

        ResolverState state = ResolverState.UNDECLARED;

        if (this.environment.find(identifier)) {
            state = this.environment.get(identifier);
        }

        return switch (state) {
            case DEFINED -> ResolverState.DEFINED;
            case DECLARED -> ResolverState.DECLARED;
            case UNDECLARED -> throw KaoriError.VariableError(identifier + " is not isDeclared", this.line);
            default -> null;
        };
    }

    @Override
    public ResolverState visitFunctionCall(ExpressionAST.FunctionCall node) {

        ResolverState state = ResolverState.UNDECLARED;

        return ResolverState.DEFINED;
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.environment.enterScope();
        this.visitStatements(statement.statements);
        this.environment.exitScope();
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        String identifier = statement.left.value;
        ResolverState state = ResolverState.UNDECLARED;

        if (this.environment.find(identifier)) {
            state = this.environment.get(identifier);
        }

        if (state == ResolverState.DECLARED || state == ResolverState.DEFINED) {
            throw KaoriError.VariableError(identifier + " is already isDeclared", this.line);

        }

        ResolverState right = statement.right.acceptVisitor(this);

        this.environment.declare(identifier);
        this.environment.define(identifier, right);
    }

    @Override
    public void visitExpressionStatement(StatementAST.Expr statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitIfStatement(StatementAST.If statement) {
        statement.condition.acceptVisitor(this);
        statement.thenBranch.acceptVisitor(this);
        statement.elseBranch.acceptVisitor(this);
    }

    @Override
    public void visitWhileLoopStatement(StatementAST.WhileLoop statement) {
        statement.condition.acceptVisitor(this);
        statement.block.acceptVisitor(this);
    }

    @Override
    public void visitForLoopStatement(StatementAST.ForLoop statement) {
        statement.variable.acceptVisitor(this);
        statement.condition.acceptVisitor(this);
        statement.block.acceptVisitor(this);
        statement.increment.acceptVisitor(this);
    }

    @Override
    public void visitFunctionStatement(StatementAST.Function statement) {
        String identifier = statement.name.value;

        if (statement.block == null) {
            if (this.environment.isDeclared(identifier)) {
                throw KaoriError.VariableError(identifier + " is already isDeclared", this.line);
            }

            this.environment.declare(identifier);
            this.environment.define(identifier, ResolverState.DECLARED);
            return;
        }

        if (!this.environment.isDeclared(identifier)) {
            this.environment.declare(identifier);
            this.environment.define(identifier, ResolverState.DEFINED);
        }

        if (this.environment.get(identifier) == ResolverState.DEFINED) {
            throw KaoriError.VariableError(identifier + " is already defined", this.line);
        }

        this.environment.define(identifier, ResolverState.DEFINED);

        this.environment.enterScope();

        for (StatementAST.Variable parameter : statement.parameters) {
            parameter.acceptVisitor(this);
        }

        List<StatementAST> statements = statement.block.statements;

        this.visitStatements(statements);

        this.environment.exitScope();
    }

    @Override
    public void visitFunctionDeclStatement(StatementAST.FunctionDecl statement) {
        String identifier = statement.name.value;

        if (this.environment.isDeclared(identifier)) {
            throw KaoriError.VariableError(identifier + " is already declared", this.line);
        }

        this.environment.declare(identifier);
        this.environment.define(identifier, ResolverState.DECLARED);
    }

}
