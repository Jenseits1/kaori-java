package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;
import com.kaori.parser.TypeAST;

public class Interpreter extends Visitor<Object> {
    public Interpreter(List<StatementAST> statements) {
        super(statements);
    }

    @Override
    protected void declare(ExpressionAST.Identifier node) {
        String identifier = node.value;

        this.callStack.declare(identifier);
    }

    @Override
    protected Object define(ExpressionAST.Identifier node, Object value) {
        String identifier = node.value;

        return this.callStack.define(identifier, value);
    }

    @Override
    protected Object get(ExpressionAST.Identifier node) {
        String identifier = node.value;

        return this.callStack.get(identifier);
    }

    @Override
    public Object visitBinaryOperator(ExpressionAST.BinaryOperator node) {
        Object left = node.left.acceptVisitor(this); // already‑evaluated values
        Object right = node.right.acceptVisitor(this);

        return switch (node.operator) {
            case PLUS -> (Double) left + (Double) right;
            case MINUS -> (Double) left - (Double) right;
            case MULTIPLY -> (Double) left * (Double) right;
            case DIVIDE -> (Double) left / (Double) right;
            case MODULO -> (Double) left % (Double) right;
            case GREATER -> (Double) left > (Double) right;
            case GREATER_EQUAL -> (Double) left >= (Double) right;
            case LESS -> (Double) left < (Double) right;
            case LESS_EQUAL -> (Double) left <= (Double) right;
            case AND -> (Boolean) left && (Boolean) right;
            case OR -> (Boolean) left || (Boolean) right;
            case EQUAL -> left.equals(right);
            case NOT_EQUAL -> !left.equals(right);
            case ASSIGN -> this.define((ExpressionAST.Identifier) node.left, right);
            default -> null;
        };
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal node) {
        return node.value;
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier node) {
        return this.get(node);
    }

    @Override
    public Object visitNot(ExpressionAST.Not node) {
        Object value = node.left.acceptVisitor(this);
        return !(Boolean) value;
    }

    @Override
    public Object visitNegation(ExpressionAST.Negation node) {
        Object left = node.left.acceptVisitor(this);
        return -(Double) left;
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        Object expression = statement.expression.acceptVisitor(this);
        System.out.println(expression);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.callStack.enterScope();
        this.visitStatements(statement.statements);
        this.callStack.leaveScope();
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        Object value = statement.right.acceptVisitor(this);
        this.declare(statement.left);
        this.define(statement.left, value);
    }

    @Override
    public void visitExpressionStatement(StatementAST.Expr statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitIfStatement(StatementAST.If statement) {
        Object condition = statement.condition.acceptVisitor(this);

        if ((Boolean) condition == true) {
            statement.thenBranch.acceptVisitor(this);
        } else {
            statement.elseBranch.acceptVisitor(this);
        }
    }

    @Override
    public void visitWhileLoopStatement(StatementAST.WhileLoop statement) {
        while (true) {
            Object condition = statement.condition.acceptVisitor(this);

            if ((Boolean) condition == false)
                break;

            statement.block.acceptVisitor(this);
        }
    }

    @Override
    public void visitForLoopStatement(StatementAST.ForLoop statement) {
        statement.variable.acceptVisitor(this);

        while (true) {
            Object condition = statement.condition.acceptVisitor(this);

            if ((Boolean) condition == false)
                break;

            statement.block.acceptVisitor(this);
            statement.increment.acceptVisitor(this);
        }
    }

    @Override
    public void visitFunctionStatement(StatementAST.Function statement) {
        // Future implementation
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall node) {
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
    }
}
