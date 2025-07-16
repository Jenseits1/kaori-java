package com.kaori.visitor;

import java.util.List;

import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.error.KaoriError;
import com.kaori.memory.Environment;

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
    protected void declare(ExpressionAST.Identifier identifier, ResolverState value) {
        int distance = this.environment.distance(identifier);

        if (distance == 0) {
            throw KaoriError.ResolveError(identifier.value() + " is already declared", this.line);
        }

        this.environment.put(identifier, value);
    }

    @Override
    protected void define(ExpressionAST.Identifier identifier, ResolverState value) {
        int distance = this.environment.distance(identifier);

        if (distance < 0) {
            throw KaoriError.ResolveError(identifier.value() + " is not declared", this.line);
        }

        this.environment.put(identifier, value, distance);
    }

    @Override
    protected ResolverState get(ExpressionAST.Identifier identifier) {
        int distance = this.environment.distance(identifier);

        if (distance < 0) {
            throw KaoriError.ResolveError(identifier.value() + " is not declared", this.line);
        }

        ResolverState state = this.environment.get(identifier, distance);

        return state;
    }

    @Override
    public ResolverState visitBinaryOperator(ExpressionAST.BinaryOperator node) {
        this.visit(node.left());
        this.visit(node.right());

        return ResolverState.DEFINED;
    }

    @Override
    public ResolverState visitUnaryOperator(ExpressionAST.UnaryOperator node) {
        this.visit(node.left());

        return ResolverState.DEFINED;
    }

    @Override
    public ResolverState visitAssign(ExpressionAST.Assign node) {
        ExpressionAST.Identifier identifier = node.left();

        this.visit(node.right());

        this.define(identifier, ResolverState.DEFINED);

        return ResolverState.DEFINED;
    }

    @Override
    public ResolverState visitLiteral(ExpressionAST.Literal node) {
        if (node.value() == null) {
            return ResolverState.DECLARED;
        }

        return ResolverState.DEFINED;
    }

    @Override
    public ResolverState visitIdentifier(ExpressionAST.Identifier node) {
        return this.get(node);
    }

    @Override
    public ResolverState visitFunctionCall(ExpressionAST.FunctionCall node) {
        this.visit(node.callee());

        for (ExpressionAST argument : node.arguments()) {
            this.visit(argument);
        }

        return ResolverState.DEFINED;
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

        this.declare(identifier, ResolverState.DECLARED);
        ResolverState right = this.visit(statement.right());
        this.define(identifier, right);
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

        int distance = this.environment.distance(identifier);

        if (distance != 0) {
            this.declare(identifier, ResolverState.DECLARED);
        }

        this.define(identifier, ResolverState.DEFINED);

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

        this.declare(identifier, ResolverState.DECLARED);
    }
}
