package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.ExpressionAST.Identifier;
import com.kaori.parser.StatementAST;
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
    protected void declare(ExpressionAST.Identifier identifier, ResolverState value) {
        int distance = this.environment.distance(identifier);

        if (distance == 0) {
            throw KaoriError.VariableError(identifier.value + " is already declared", this.line);
        }

        this.environment.put(identifier, value);
    }

    @Override
    protected void define(ExpressionAST.Identifier identifier, ResolverState value) {
        int distance = this.environment.distance(identifier);

        if (distance < 0) {
            throw KaoriError.VariableError(identifier.value + " is not declared", this.line);
        }

        this.environment.put(identifier, value, distance);
    }

    @Override
    protected ResolverState get(ExpressionAST.Identifier identifier) {
        int distance = this.environment.distance(identifier);

        if (distance < 0) {
            throw KaoriError.VariableError(identifier.value + " is not declared", this.line);
        }

        ResolverState state = this.environment.get(identifier, distance);

        return state;
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

        this.define(node.left, ResolverState.DEFINED);

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
        return this.get(node);
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
        ExpressionAST.Identifier identifier = statement.left;

        this.declare(identifier, ResolverState.DECLARED);
        ResolverState right = statement.right.acceptVisitor(this);
        this.define(identifier, right);
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
        ExpressionAST.Identifier identifier = statement.name;

        int distance = this.environment.distance(identifier);

        if (distance != 0) {
            this.declare(identifier, ResolverState.DECLARED);
        }

        this.define(identifier, ResolverState.DEFINED);

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
        ExpressionAST.Identifier identifier = statement.name;

        this.declare(identifier, ResolverState.DECLARED);
    }
}
