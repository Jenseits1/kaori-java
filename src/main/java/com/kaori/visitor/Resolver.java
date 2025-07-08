package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;

public class Resolver extends Visitor<Resolver.ResolutionState> {
    public Resolver(List<StatementAST> statements) {
        super(statements);
    }

    static public enum ResolutionState {
        UNDECLARED,
        DECLARED,
        INITIALIZED,

    }

    @Override
    public ResolutionState visitAdd(ExpressionAST.Add node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitSubtract(ExpressionAST.Subtract node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitMultiply(ExpressionAST.Multiply node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitDivide(ExpressionAST.Divide node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitModulo(ExpressionAST.Modulo node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitAnd(ExpressionAST.And node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitOr(ExpressionAST.Or node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitEqual(ExpressionAST.Equal node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitNotEqual(ExpressionAST.NotEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitGreater(ExpressionAST.Greater node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitGreaterEqual(ExpressionAST.GreaterEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitLess(ExpressionAST.Less node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitLessEqual(ExpressionAST.LessEqual node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitAssign(ExpressionAST.Assign node) {
        node.left.acceptVisitor(this);
        node.right.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitLiteral(ExpressionAST.Literal node) {
        if (node.value == null) {
            return ResolutionState.DECLARED;
        }

        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitIdentifier(ExpressionAST.Identifier node) {
        Environment<ResolutionState> env = this.environment.find(node);

        ResolutionState value = env.get(node);

        if (value == null)
            value = ResolutionState.UNDECLARED;

        if (value == ResolutionState.UNDECLARED) {
            throw KaoriError.VariableError("expected " + node.value + " to be declared", this.line);
        } else if (value == ResolutionState.DECLARED) {
            throw KaoriError.VariableError("expected " + node.value + " to be initialized", this.line);
        }

        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitNot(ExpressionAST.Not node) {
        node.left.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
    }

    @Override
    public ResolutionState visitNegation(ExpressionAST.Negation node) {
        node.left.acceptVisitor(this);
        return ResolutionState.INITIALIZED;
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
        ResolutionState value = this.environment.get(statement.left);

        if (value == null)
            value = ResolutionState.UNDECLARED;

        if (value == ResolutionState.UNDECLARED) {
            ResolutionState right = statement.right.acceptVisitor(this);

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
        ResolutionState value = this.environment.get(statement.name);

        if (value == null) value = ResolutionState.UNDECLARED;

        if (value == ResolutionState.UNDECLARED) {
            this.environment.set(, value);
        }
    }

    @Override
    public ResolutionState visitFunctionCall(ExpressionAST.FunctionCall node) {
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
    }
}
