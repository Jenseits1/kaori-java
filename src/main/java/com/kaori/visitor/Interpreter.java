package com.kaori.visitor;

import java.util.List;

import com.kaori.parser.ExpressionAST;
import com.kaori.parser.ExpressionAST.Identifier;
import com.kaori.parser.StatementAST;
import com.kaori.parser.StatementAST.FunctionDecl;

public class Interpreter extends Visitor<Object> {
    public Interpreter(List<StatementAST> statements) {
        super(statements);
    }

    @Override
    public Object visitBinaryOperator(ExpressionAST.BinaryOperator node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);
        ExpressionAST.Operator operator = node.operator;

        return switch (operator) {
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
            default -> null;
        };
    }

    @Override
    public Object visitUnaryOperator(ExpressionAST.UnaryOperator node) {
        Object left = node.left.acceptVisitor(this);
        ExpressionAST.Operator operator = node.operator;

        return switch (operator) {
            case MINUS -> -(Double) left;
            case NOT -> !(Boolean) left;
            default -> null;
        };
    }

    @Override
    public Object visitAssign(ExpressionAST.Assign node) {
        Object value = node.right.acceptVisitor(this);

        return value;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal node) {
        return node.value;
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier node) {
        return null;
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall node) {
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        Object expression = statement.expression.acceptVisitor(this);
        System.out.println(expression);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {

        this.visitStatements(statement.statements);

    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        Object value = statement.right.acceptVisitor(this);

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

    }

    @Override
    public void visitFunctionDeclStatement(FunctionDecl statement) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionDeclStatement'");
    }

    @Override
    protected void declare(Identifier identifier, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'declare'");
    }

    @Override
    protected void define(Identifier identifier, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'define'");
    }

    @Override
    protected Object get(Identifier identifier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }
}
