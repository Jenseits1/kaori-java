package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.Expression;
import com.kaori.parser.Statement;

public class TypeChecker extends Visitor<TypeChecker.KaoriType> {
    public TypeChecker(List<Statement> statements) {
        super(statements, new Environment());
    }

    public enum KaoriType {
        STRING,
        NUMBER,
        BOOLEAN,
        FUNCTION
    }

    @Override
    public KaoriType visitComma(Expression.Comma node) {
        Object right = node.right.acceptVisitor(this);

        return (KaoriType) right;
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

        throw KaoriError.TypeError("expected number or string operands for '+'", this.line);
    }

    @Override
    public KaoriType visitSubtract(Expression.Subtract node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '-'", this.line);
    }

    @Override
    public KaoriType visitMultiply(Expression.Multiply node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '*'", this.line);
    }

    @Override
    public KaoriType visitDivide(Expression.Divide node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '/'", this.line);
    }

    @Override
    public KaoriType visitModulo(Expression.Modulo node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '%'", this.line);
    }

    @Override
    public KaoriType visitAnd(Expression.And node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.BOOLEAN && right == KaoriType.BOOLEAN) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operands for '&&'", this.line);
    }

    @Override
    public KaoriType visitOr(Expression.Or node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.BOOLEAN && right == KaoriType.BOOLEAN) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operands for '||'", this.line);
    }

    @Override
    public KaoriType visitEqual(Expression.Equal node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == right) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected operands of same type for '=='", this.line);
    }

    @Override
    public KaoriType visitNotEqual(Expression.NotEqual node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == right) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected operands of same type for '!='", this.line);
    }

    @Override
    public KaoriType visitGreater(Expression.Greater node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>'", this.line);
    }

    @Override
    public KaoriType visitGreaterEqual(Expression.GreaterEqual node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>='", this.line);
    }

    @Override
    public KaoriType visitLess(Expression.Less node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<'", this.line);
    }

    @Override
    public KaoriType visitLessEqual(Expression.LessEqual node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == KaoriType.NUMBER && right == KaoriType.NUMBER) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<='", this.line);
    }

    @Override
    public KaoriType visitAssign(Expression.Assign node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left == right) {
            return left;
        }

        throw KaoriError.TypeError("expected different value type in variable assignment", this.line);
    }

    @Override
    public KaoriType visitBooleanLiteral(Expression.BooleanLiteral node) {
        return KaoriType.BOOLEAN;
    }

    @Override
    public KaoriType visitNumberLiteral(Expression.NumberLiteral node) {
        return KaoriType.NUMBER;
    }

    @Override
    public KaoriType visitStringLiteral(Expression.StringLiteral node) {
        return KaoriType.STRING;
    }

    @Override
    public KaoriType visitIdentifier(Expression.Identifier node) {
        Object value = environment.get(node.value, this.line);

        return (KaoriType) value;
    }

    @Override
    public KaoriType visitNot(Expression.Not node) {
        KaoriType value = node.left.acceptVisitor(this);

        if (value == KaoriType.BOOLEAN) {
            return KaoriType.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operand for '!'", this.line);
    }

    @Override
    public KaoriType visitNegation(Expression.Negation node) {
        KaoriType left = node.left.acceptVisitor(this);

        if (left == KaoriType.NUMBER) {
            return KaoriType.NUMBER;
        }

        throw KaoriError.TypeError("expected float operand for unary '-'", this.line);
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
    public void visitIfStatement(Statement.If statement) {
        KaoriType condition = statement.condition.acceptVisitor(this);

        if (condition != KaoriType.BOOLEAN) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.ifBranch.acceptVisitor(this);
        statement.elseBranch.acceptVisitor(this);

    }

    @Override
    public void visitWhileLoopStatement(Statement.WhileLoop statement) {
        KaoriType condition = statement.condition.acceptVisitor(this);

        if (condition != KaoriType.BOOLEAN) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.block.acceptVisitor(this);
    }

    @Override
    public void visitForLoopStatement(Statement.ForLoop statement) {
        statement.variable.acceptVisitor(this);

        KaoriType condition = statement.condition.acceptVisitor(this);

        if (condition != KaoriType.BOOLEAN) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.block.acceptVisitor(this);
        statement.increment.acceptVisitor(this);

    }

    @Override
    public KaoriType visitFunctionLiteral(Expression.FunctionLiteral functionLiteral) {
        return KaoriType.FUNCTION;
    }
}
