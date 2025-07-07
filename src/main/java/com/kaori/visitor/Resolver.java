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
        return null;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal node) {
        return null;
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier node) {
        Environment<Object> env = this.environment.find(node);

        if (env.get(node) == null) {
            throw KaoriError.VariableError("expected " + node.value + " to be declared", this.line);
        }

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
        this.environment = new Environment<>(environment);
        this.visitStatements(statement.statements);
        this.environment = environment.getPrevious();
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        if (this.environment.get(statement.left) == null) {
            statement.right.acceptVisitor(this);
            this.environment.set(statement.left, 1);
        } else {
            throw KaoriError.VariableError(statement.left.value + " is already declared", this.line);
        }
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
        // Function resolution logic here, if applicable
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall node) {
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
    }
}
