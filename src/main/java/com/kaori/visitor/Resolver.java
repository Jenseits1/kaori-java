package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;

public class Resolver extends Visitor<Resolver.Defined> {
    public Resolver(List<StatementAST> statements) {
        super(statements);
    }

    static public enum Defined {
        TRUE,
        FALSE
    }

    @Override
    public Defined visitAdd(ExpressionAST.Add node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitSubtract(ExpressionAST.Subtract node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitMultiply(ExpressionAST.Multiply node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitDivide(ExpressionAST.Divide node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitModulo(ExpressionAST.Modulo node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitAnd(ExpressionAST.And node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitOr(ExpressionAST.Or node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitEqual(ExpressionAST.Equal node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitNotEqual(ExpressionAST.NotEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitGreater(ExpressionAST.Greater node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitGreaterEqual(ExpressionAST.GreaterEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitLess(ExpressionAST.Less node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitLessEqual(ExpressionAST.LessEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitAssign(ExpressionAST.Assign node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitLiteral(ExpressionAST.Literal node) {
        if (node.value == null) {
            return Defined.FALSE;
        }

        return Defined.TRUE;
    }

    @Override
    public Defined visitIdentifier(ExpressionAST.Identifier node) {
        Environment<Defined> env = this.environment.find(node);

        if (env.get(node) == null) {
            throw KaoriError.VariableError("expected " + node.value + " to be declared", this.line);
        } else if (env.get(node) == Defined.FALSE) {
            throw KaoriError.VariableError("expected " + node.value + " to be defined", this.line);
        }

        return Defined.TRUE;
    }

    @Override
    public Defined visitNot(ExpressionAST.Not node) {
        node.left.acceptVisitor(this);
        return Defined.TRUE;
    }

    @Override
    public Defined visitNegation(ExpressionAST.Negation node) {
        node.left.acceptVisitor(this);
        return Defined.TRUE;
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
            Defined right = statement.right.acceptVisitor(this);

            this.environment.set(statement.left, right);
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
    public Defined visitFunctionCall(ExpressionAST.FunctionCall node) {
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
    }
}
