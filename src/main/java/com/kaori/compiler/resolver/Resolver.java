package com.kaori.compiler.resolver;

import java.util.List;

import com.kaori.ast.DeclarationAST;

import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.compiler.Visitor;
import com.kaori.error.KaoriError;

public class Resolver extends Visitor<ResolutionStatus> {
    protected final Environment environment;

    public Resolver(List<DeclarationAST> declarations) {
        super(declarations);
        this.environment = new Environment();

    }

    /* Expressions */
    @Override
    public ResolutionStatus visitBinaryExpression(ExpressionAST.BinaryExpression expression) {
        this.visit(expression.left());
        this.visit(expression.right());

        return ResolutionStatus.RESOLVED;
    }

    @Override
    public ResolutionStatus visitUnaryExpression(ExpressionAST.UnaryExpression expression) {
        this.visit(expression.left());

        return ResolutionStatus.RESOLVED;
    }

    @Override
    public ResolutionStatus visitAssign(ExpressionAST.Assign expression) {
        this.visit(expression.left());
        this.visit(expression.right());

        return ResolutionStatus.RESOLVED;
    }

    @Override
    public ResolutionStatus visitLiteral(ExpressionAST.Literal expression) {
        return ResolutionStatus.RESOLVED;
    }

    @Override
    public ResolutionStatus visitIdentifier(ExpressionAST.Identifier expression) {
        DeclarationRef reference = this.environment.search(expression.name());
        ResolutionStatus status = this.environment.status(reference);

        if (status == ResolutionStatus.UNRESOLVED) {
            throw KaoriError.ResolveError(expression.name() + " is not declared", this.line);
        }

        expression.setReference(reference);

        return ResolutionStatus.RESOLVED;
    }

    @Override
    public ResolutionStatus visitFunctionCall(ExpressionAST.FunctionCall expression) {
        this.visit(expression.callee());

        for (ExpressionAST argument : expression.arguments()) {
            this.visit(argument);
        }

        return ResolutionStatus.RESOLVED;
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

    /* Declarations */
    @Override
    public void visitVariableDeclaration(DeclarationAST.Variable declaration) {
        ExpressionAST.Identifier identifier = declaration.left();

        this.visit(declaration.right());

        DeclarationRef reference = this.environment.searchInner(identifier.name());
        ResolutionStatus status = this.environment.status(reference);

        switch (status) {
            case RESOLVED ->
                throw KaoriError.ResolveError(identifier.name() + " is already declared", this.line);
            case UNRESOLVED -> this.environment.declare(identifier.name());
        }
    }

    @Override
    public void visitFunctionDeclaration(DeclarationAST.Function declaration) {
        ExpressionAST.Identifier identifier = declaration.name();
        DeclarationRef reference = this.environment.searchInner(identifier.name());
        ResolutionStatus status = this.environment.status(reference);

        if (status == ResolutionStatus.RESOLVED) {
            throw KaoriError.ResolveError(identifier.name() + " is already declared", this.line);
        }

        this.environment.declare(identifier.name());
    }

    @Override
    public void visitFunctionDefinition(DeclarationAST.Function declaration) {
        this.environment.enterFunction();

        for (DeclarationAST.Variable parameter : declaration.parameters()) {
            this.visit(parameter);
        }

        List<DeclarationAST> declarations = declaration.block().declarations();

        this.visitDeclarations(declarations);

        this.environment.exitFunction();
    }

}
