package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;

public class Resolver extends Visitor<Object> {
    public Resolver(List<StatementAST> statements) {
        super(statements);
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
    protected Object define(ExpressionAST.Identifier node, Object value) {
        String identifier = node.value;

        if (this.callStack.find(identifier)) {
            return this.callStack.define(identifier, value);
        } else {
            throw KaoriError.VariableError(identifier + " is not declared", this.line);
        }
    }

    @Override
    protected Object get(ExpressionAST.Identifier node) {
        String identifier = node.value;

        if (this.callStack.find(identifier)) {
            return this.callStack.get(identifier);
        } else {
            throw KaoriError.VariableError(identifier + " is not declared", this.line);
        }
    }

    @Override
    public Object visitBinaryOperator(ExpressionAST.BinaryOperator node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return node;
    }

    @Override
    public Object visitUnaryOperator(ExpressionAST.UnaryOperator node) {
        node.left.acceptVisitor(this);

        return node;
    }

    @Override
    public Object visitAssign(ExpressionAST.Assign node) {
        node.right.acceptVisitor(this);

        return this.define(node.left, node.right);
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal node) {
        if (node.value == null) {
            return null;
        }

        return node;
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier node) {
        Object value = this.get(node);

        if (value == null) {
            throw KaoriError.VariableError(node.value + " is not defined", this.line);
        }

        return node;
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall node) {
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
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
        Object right = statement.right.acceptVisitor(this);

        this.declare(statement.left);

        if (right == null) {
            return;
        }

        this.define(statement.left, statement.right);
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
        this.define(statement.name, statement);
    }
}
