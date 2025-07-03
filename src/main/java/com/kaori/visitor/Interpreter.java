package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.Expression;
import com.kaori.parser.Statement;

public class Interpreter extends Visitor<Object> {
    public Interpreter(List<Statement> statements) {
        super(statements, new Environment());
    }

    @Override
    public Object visitAdd(Expression.Add node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left instanceof Float) {
            return (Float) left + (Float) right;
        } else {
            return (String) left + (String) right;
        }

    }

    @Override
    public Object visitSubtract(Expression.Subtract node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Float) left - (Float) right;

    }

    @Override
    public Object visitMultiply(Expression.Multiply node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Float) left * (Float) right;
    }

    @Override
    public Object visitDivide(Expression.Divide node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if ((Float) right == 0.0f) {
            throw KaoriError.DivisionByZero("can not do division by zero", this.line);
        }

        return (Float) left / (Float) right;
    }

    @Override
    public Object visitModulo(Expression.Modulo node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if ((Float) right == 0.0f) {
            throw KaoriError.DivisionByZero("can not do division by zero", this.line);
        }

        return (Float) left % (Float) right;
    }

    @Override
    public Object visitAnd(Expression.And node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Boolean) left && (Boolean) right;
    }

    @Override
    public Object visitOr(Expression.Or node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Boolean) left || (Boolean) right;
    }

    @Override
    public Object visitEqual(Expression.Equal node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return left.equals(right);
    }

    @Override
    public Object visitNotEqual(Expression.NotEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return !left.equals(right);
    }

    @Override
    public Object visitGreater(Expression.Greater node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Float) left > (Float) right;
    }

    @Override
    public Object visitGreaterEqual(Expression.GreaterEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Float) left >= (Float) right;
    }

    @Override
    public Object visitLess(Expression.Less node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Float) left < (Float) right;
    }

    @Override
    public Object visitLessEqual(Expression.LessEqual node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        return (Float) left <= (Float) right;
    }

    @Override
    public Object visitAssign(Expression.Assign node) {
        Expression.Identifier identifier = (Expression.Identifier) node.left;
        Object value = node.right.acceptVisitor(this);

        environment.set(identifier.value, value);

        return value;
    }

    @Override
    public Object visitLiteral(Expression.Literal node) {
        return node.value;
    }

    @Override
    public Object visitFunctionLiteral(Expression.FunctionLiteral node) {
        return node;
    }

    @Override
    public Object visitIdentifier(Expression.Identifier node) {
        Object value = environment.get(node.value, this.line);

        return value;
    }

    @Override
    public Object visitNot(Expression.Not node) {
        Object value = node.left.acceptVisitor(this);

        return !(Boolean) value;
    }

    @Override
    public Object visitNegation(Expression.Negation node) {
        Object left = node.left.acceptVisitor(this);

        return -(Float) left;
    }

    @Override
    public void visitPrintStatement(Statement.Print statement) {
        Object expression = statement.expression.acceptVisitor(this);

        System.out.println(expression);
    }

    @Override
    public void visitBlockStatement(Statement.Block statement) {
        this.environment = new Environment(environment);

        this.visitStatements(statement.statements);

        this.environment = environment.getPrevious();
    }

    @Override
    public void visitVariableStatement(Statement.Variable statement) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitVariableStatement'");
    }

    @Override
    public void visitExpressionStatement(Statement.Expr statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitIfStatement(Statement.If statement) {
        Object condition = statement.condition.acceptVisitor(this);

        if ((Boolean) condition == true) {
            statement.ifBranch.acceptVisitor(this);
        } else if (statement.elseBranch != null) {
            statement.elseBranch.acceptVisitor(this);
        }

    }

    @Override
    public void visitWhileLoopStatement(Statement.WhileLoop statement) {
        while (true) {
            Object condition = statement.condition.acceptVisitor(this);

            if ((Boolean) condition == false) {
                break;
            }

            statement.block.acceptVisitor(this);
        }
    }

    @Override
    public void visitForLoopStatement(Statement.ForLoop statement) {
        statement.variable.acceptVisitor(this);

        while (true) {
            Object condition = statement.condition.acceptVisitor(this);

            if ((Boolean) condition == false) {
                break;
            }

            statement.block.acceptVisitor(this);

            statement.increment.acceptVisitor(this);
        }
    }

}
