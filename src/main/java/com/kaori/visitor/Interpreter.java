package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.StatementAST;

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
    public Object visitAdd(ExpressionAST.Add node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Double) {
            return (Double) left + (Double) right;
        } else {
            return (String) left + (String) right;
        }
    }

    @Override
    public Object visitSubtract(ExpressionAST.Subtract node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Double) left - (Double) right;
    }

    @Override
    public Object visitMultiply(ExpressionAST.Multiply node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Double) left * (Double) right;
    }

    @Override
    public Object visitDivide(ExpressionAST.Divide node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if ((Double) right == 0.0f) {
            throw KaoriError.DivisionByZero(String.format("invalid / operation between %s and %s", left, right),
                    this.line);
        }

        return (Double) left / (Double) right;
    }

    @Override
    public Object visitModulo(ExpressionAST.Modulo node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if ((Double) right == 0.0f) {
            throw KaoriError.DivisionByZero(String.format("invalid % operation between %s and %s", left, right),
                    this.line);
        }

        return (Double) left % (Double) right;
    }

    @Override
    public Object visitAnd(ExpressionAST.And node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Boolean) left && (Boolean) right;
    }

    @Override
    public Object visitOr(ExpressionAST.Or node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Boolean) left || (Boolean) right;
    }

    @Override
    public Object visitEqual(ExpressionAST.Equal node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return left.equals(right);
    }

    @Override
    public Object visitNotEqual(ExpressionAST.NotEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return !left.equals(right);
    }

    @Override
    public Object visitGreater(ExpressionAST.Greater node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Double) left > (Double) right;
    }

    @Override
    public Object visitGreaterEqual(ExpressionAST.GreaterEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Double) left >= (Double) right;
    }

    @Override
    public Object visitLess(ExpressionAST.Less node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Double) left < (Double) right;
    }

    @Override
    public Object visitLessEqual(ExpressionAST.LessEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Double) left <= (Double) right;
    }

    @Override
    public Object visitAssign(ExpressionAST.Assign node) {
        Object value = node.right.acceptVisitor(this);

        this.define(node.left, value);

        return value;
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
