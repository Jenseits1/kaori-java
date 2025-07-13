package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;

public class Resolver extends Visitor<Resolver.ResolverState> {
    public Resolver(List<StatementAST> statements) {
        super(statements);

    }

    public static enum ResolverState {
        UNDECLARED,
        DECLARED,
        DEFINED
    }

    @Override
    protected void declare(ExpressionAST.Identifier node) {
        String identifier = node.value;

        if (this.callStack.declared(identifier)) {
            throw KaoriError.VariableError(identifier + " is already declared", this.line);
        }

        this.callStack.declare(identifier);
    }

    @Override
    protected ResolverState define(ExpressionAST.Identifier node, ResolverState value) {
        String identifier = node.value;

        if (this.callStack.find(identifier)) {
            return this.callStack.define(identifier, value);
        } else {
            throw KaoriError.VariableError(identifier + " is not declared", this.line);
        }
    }

    @Override
    protected ResolverState get(ExpressionAST.Identifier node) {
        String identifier = node.value;

        if (this.callStack.find(identifier)) {
            return this.callStack.get(identifier);
        } else {
            throw KaoriError.VariableError(identifier + " is not declared", this.line);
        }
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

        return this.define(node.left, ResolverState.DEFINED);
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
        ResolverState state = this.get(node);

        if (state == ResolverState.DECLARED) {
            throw KaoriError.VariableError(node.value + " is not defined", this.line);
        }

        return state;
    }

    @Override
    public ResolverState visitFunctionCall(ExpressionAST.FunctionCall node) {
        node.callee.acceptVisitor(this);

        return ResolverState.DEFINED;
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.callStack.enterScope();
        this.visitStatements(statement.statements);
        this.callStack.leaveScope();

    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        ResolverState right = statement.right.acceptVisitor(this);

        this.declare(statement.left);
        this.define(statement.left, right);
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
        this.declare(statement.name);
        this.define(statement.name, ResolverState.DEFINED);
    }
}
