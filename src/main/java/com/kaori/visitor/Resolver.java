package com.kaori.visitor;

import java.util.List;

import com.kaori.ast.DeclarationAST;
import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.error.KaoriError;
import com.kaori.memory.Environment;

public class Resolver extends Visitor<Resolver.ResolutionStatus> {
    public final Environment<ResolutionStatus> environment;

    public Resolver(List<DeclarationAST> declarations) {
        super(declarations);

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

    /* Expressions */
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

    /* Statements */
    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        this.visit(statement.expression());
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.environment.enterScope();
        this.visitDeclarations(statement.declarations());
        this.environment.exitScope();
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

    /* Declarations */
    @Override
    public void visitVariableDeclaration(DeclarationAST.Variable declaration) {
        ExpressionAST.Identifier identifier = declaration.left();

        this.visit(declaration.right());

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
    public void visitFunctionDeclaration(DeclarationAST.Function declaration) {
        ExpressionAST.Identifier identifier = declaration.name();

        int distance = this.environment.searchInner(identifier.name());

        ResolutionStatus status = this.getStatus(distance);

        if (status == ResolutionStatus.DECLARED) {
            throw KaoriError.ResolveError(identifier.name() + " is already declared", this.line);
        }

        this.environment.define(identifier.name(), ResolutionStatus.DECLARED, distance);

        identifier.setDistance(distance);

        this.environment.enterScope();

        for (DeclarationAST.Variable parameter : declaration.parameters()) {
            this.visit(parameter);
        }

        List<DeclarationAST> declarations = declaration.block().declarations();

        this.visitDeclarations(declarations);

        this.environment.exitScope();
    }

}
