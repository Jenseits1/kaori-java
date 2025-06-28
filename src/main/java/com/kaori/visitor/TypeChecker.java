package com.kaori.visitor;

import java.util.List;

import com.kaori.Environment;
import com.kaori.KaoriError;
import com.kaori.KaoriType;
import com.kaori.ast.Expression;
import com.kaori.ast.Statement;

public class TypeChecker extends Visitor<KaoriType> {
    public TypeChecker(List<Statement> statements) {
        super(statements, new Environment());
    }

    @Override
    public KaoriType visitLiteral(Expression.Literal node) {
        if (node.value instanceof Float) {
            return KaoriType.NUMBER;
        } else if (node.value instanceof String) {
            return KaoriType.STRING;
        } else if (node.value instanceof Boolean) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("unrecognized type", line);
    }

    @Override
    public KaoriType visitAdd(Expression.Add node) {
        Object left = node.left.acceptVisitor(this);
        Object right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        if (left == KaoriType.STRING && right == KaoriType.STRING) {
            return KaoriType.STRING;
        }

        throw KaoriError.TypeError("expected number or string operands for '+'", line);
    }

    @Override
    public KaoriType visitSubtract(Expression.Subtract node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '-'", line);
    }

    @Override
    public KaoriType visitMultiply(Expression.Multiply node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '*'", line);
    }

    @Override
    public KaoriType visitDivide(Expression.Divide node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '/'", line);
    }

    @Override
    public KaoriType visitModulo(Expression.Modulo node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '%'", line);
    }

    @Override
    public KaoriType visitAnd(Expression.And node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.BOOLEAN && right == KaoriType.BOOLEAN) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operands for '&&'", line);
    }

    @Override
    public KaoriType visitOr(Expression.Or node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.BOOLEAN && right == KaoriType.BOOLEAN) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operands for '||'", line);
    }

    @Override
    public KaoriType visitEqual(Expression.Equal node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == right) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected operands of same type for '=='", line);
    }

    @Override
    public KaoriType visitNotEqual(Expression.NotEqual node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == right) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected operands of same type for '!='", line);
    }

    @Override
    public KaoriType visitGreater(Expression.Greater node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>'", line);
    }

    @Override
    public KaoriType visitGreaterEqual(Expression.GreaterEqual node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>='", line);
    }

    @Override
    public KaoriType visitLess(Expression.Less node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<'", line);
    }

    @Override
    public KaoriType visitLessEqual(Expression.LessEqual node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<='", line);
    }

    @Override
    public KaoriType visitAssign(Expression.Assign node) {
        KaoriType leftType = node.left.acceptVisitor(this);
        KaoriType rightType = node.right.acceptVisitor(this);

        if (leftType == rightType) {
            return leftType;
        }

        throw KaoriError.TypeError("expected different value type in variable assignment", line);
    }

    @Override
    public KaoriType visitIdentifier(Expression.Identifier node) {
        Object value = environment.get(node.value, line);

        return (KaoriType) value;
    }

    @Override
    public KaoriType visitNot(Expression.Not node) {
        KaoriType value = node.left.acceptVisitor(this);

        if (value == KaoriType.BOOLEAN) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operand for '!'", line);
    }

    @Override
    public KaoriType visitNegation(Expression.Negation node) {
        KaoriType left = node.left.acceptVisitor(this);

        if (left == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected float operand for unary '-'", line);
    }

    @Override
    public void visitPrintStatement(Statement.Print statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitBlockStatement(Statement.Block statement) {
        this.environment = new Environment(environment);

        this.visitStatements(statement.statements);

        this.environment = environment.getPrevious();
    }

    @Override
    public void visitExpressionStatement(Statement.Expr statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitVariableStatement(Statement.Variable statement) {
        String identifier = statement.left.value;
        KaoriType value = statement.right.acceptVisitor(this);

        environment.declare(identifier, value, line);
    }

    @Override
    public void visitIfStatement(Statement.If statement) {
        KaoriType condition = statement.condition.acceptVisitor(this);

        if (condition != KaoriType.BOOLEAN) {
            throw KaoriError.TypeError("expected boolean value for condition", line);
        }

        statement.ifBranch.acceptVisitor(this);
        statement.elseBranch.acceptVisitor(this);

    }

    @Override
    public void visitWhileLoopStatement(Statement.WhileLoop statement) {
        KaoriType condition = statement.condition.acceptVisitor(this);

        if (condition != KaoriType.BOOLEAN) {
            throw KaoriError.TypeError("expected boolean value for condition", line);
        }

        statement.block.acceptVisitor(this);
    }

    @Override
    public void visitForLoopStatement(Statement.ForLoop statement) {
        statement.variable.acceptVisitor(this);

        KaoriType condition = statement.condition.acceptVisitor(this);

        if (condition != KaoriType.BOOLEAN) {
            throw KaoriError.TypeError("expected boolean value for condition", line);
        }

        statement.block.acceptVisitor(this);
        statement.increment.acceptVisitor(this);

    }
}
