package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;
import com.kaori.visitor.memory.StackFrame;

public class Resolver extends Visitor<Object> {

    public Resolver(List<StatementAST> statements) {
        super(statements);
    }

    @Override
    protected void declare(ExpressionAST.Identifier node) {
        StackFrame<Object> currentFrame = this.callStack.currentFrame();
        String identifier = node.value;

        if (currentFrame.isDeclared(identifier)) {
            throw KaoriError.VariableError(identifier + " is already declared", this.line);
        }

        currentFrame.declare(identifier);
    }

    @Override
    protected void define(ExpressionAST.Identifier node, Object value) {
        StackFrame<Object> currentFrame = this.callStack.currentFrame();
        StackFrame<Object> mainFrame = this.callStack.mainFrame();
        String identifier = node.value;

        if (currentFrame.find(identifier)) {
            currentFrame.define(identifier, value);
        } else if (mainFrame.find(identifier)) {
            mainFrame.define(identifier, value);
        } else {
            throw KaoriError.VariableError(identifier + " is not defined", this.line);
        }
    }

    @Override
    protected Object get(ExpressionAST.Identifier node) {
        StackFrame<Object> currentFrame = this.callStack.currentFrame();
        StackFrame<Object> mainFrame = this.callStack.mainFrame();
        String identifier = node.value;

        if (currentFrame.find(identifier)) {
            return currentFrame.get(identifier);
        } else if (mainFrame.find(identifier)) {
            return mainFrame.get(identifier);
        } else {
            throw KaoriError.VariableError(identifier + " is not declared", this.line);
        }
    }

    @Override
    public Object visitAdd(ExpressionAST.Add node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        return null;
    }

    @Override
    public Object visitSubtract(ExpressionAST.Subtract node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitMultiply(ExpressionAST.Multiply node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitDivide(ExpressionAST.Divide node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitModulo(ExpressionAST.Modulo node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitAnd(ExpressionAST.And node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitOr(ExpressionAST.Or node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitEqual(ExpressionAST.Equal node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitNotEqual(ExpressionAST.NotEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitGreater(ExpressionAST.Greater node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitGreaterEqual(ExpressionAST.GreaterEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitLess(ExpressionAST.Less node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitLessEqual(ExpressionAST.LessEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitAssign(ExpressionAST.Assign node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);

        this.define(node.left, null);

        return null;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal node) {
        return null;
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier node) {
        this.get(node);
        return null;
    }

    @Override
    public Object visitNot(ExpressionAST.Not node) {
        node.left.acceptVisitor(this);
        return null;
    }

    @Override
    public Object visitNegation(ExpressionAST.Negation node) {
        node.left.acceptVisitor(this);
        return null;
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        StackFrame<Object> currentFrame = this.callStack.currentFrame();

        currentFrame.enterScope();
        this.visitStatements(statement.statements);
        currentFrame.leaveScope();
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        statement.right.acceptVisitor(this);
        this.declare(statement.left);
        this.define(statement.left, null);
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

    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall node) {
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
    }
}
