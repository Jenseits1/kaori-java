package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.Expression;
import com.kaori.parser.Expression.FunctionCall;
import com.kaori.parser.KaoriType;
import com.kaori.parser.Statement;

public class TypeChecker extends Visitor<KaoriType> {
    public TypeChecker(List<Statement> statements) {
        super(statements);
    }

    @Override
    public KaoriType visitAdd(Expression.Add node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER) && right.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.NUMBER;
        }

        if (left.equals(KaoriType.Primitive.STRING) && right.equals(KaoriType.Primitive.STRING)) {
            return KaoriType.Primitive.STRING;
        }

        throw KaoriError.TypeError("expected number or string operands for '+'", this.line);
    }

    @Override
    public KaoriType visitSubtract(Expression.Subtract node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER) && right.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '-'", this.line);
    }

    @Override
    public KaoriType visitMultiply(Expression.Multiply node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER) && right.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '*'", this.line);
    }

    @Override
    public KaoriType visitDivide(Expression.Divide node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER) && right.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '/'", this.line);
    }

    @Override
    public KaoriType visitModulo(Expression.Modulo node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER) && right.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '%'", this.line);
    }

    @Override
    public KaoriType visitAnd(Expression.And node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.BOOLEAN) && right.equals(KaoriType.Primitive.BOOLEAN)) {
            return KaoriType.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operands for '&&'", this.line);
    }

    @Override
    public KaoriType visitOr(Expression.Or node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.BOOLEAN) && right.equals(KaoriType.Primitive.BOOLEAN)) {
            return KaoriType.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operands for '||'", this.line);
    }

    @Override
    public KaoriType visitEqual(Expression.Equal node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(right)) {
            return KaoriType.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected operands of same type for '=='", this.line);
    }

    @Override
    public KaoriType visitNotEqual(Expression.NotEqual node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (!left.equals(right)) {
            return KaoriType.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected operands of same type for '!='", this.line);
    }

    @Override
    public KaoriType visitGreater(Expression.Greater node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER) && right.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>'", this.line);
    }

    @Override
    public KaoriType visitGreaterEqual(Expression.GreaterEqual node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER) && right.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>='", this.line);
    }

    @Override
    public KaoriType visitLess(Expression.Less node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER) && right.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<'", this.line);
    }

    @Override
    public KaoriType visitLessEqual(Expression.LessEqual node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER) && right.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<='", this.line);
    }

    @Override
    public KaoriType visitAssign(Expression.Assign node) {
        KaoriType left = node.left.acceptVisitor(this);
        KaoriType right = node.right.acceptVisitor(this);

        if (left.equals(right)) {
            return right;
        }

        throw KaoriError.TypeError("expected the same type on variable assignment", this.line);
    }

    @Override
    public KaoriType visitLiteral(Expression.Literal node) {
        return node.type;
    }

    @Override
    public KaoriType visitIdentifier(Expression.Identifier node) {
        Environment<KaoriType> env = this.environment.find(node);

        return env.get(node);
    }

    @Override
    public KaoriType visitNot(Expression.Not node) {
        KaoriType value = node.left.acceptVisitor(this);

        if (value.equals(KaoriType.Primitive.BOOLEAN)) {
            return KaoriType.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operand for '!'", this.line);
    }

    @Override
    public KaoriType visitNegation(Expression.Negation node) {
        KaoriType left = node.left.acceptVisitor(this);

        if (left.equals(KaoriType.Primitive.NUMBER)) {
            return KaoriType.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected float operand for unary '-'", this.line);
    }

    @Override
    public void visitPrintStatement(Statement.Print statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitBlockStatement(Statement.Block statement) {
        this.environment = new Environment<KaoriType>(environment);

        this.visitStatements(statement.statements);

        this.environment = environment.getPrevious();
    }

    @Override
    public void visitVariableStatement(Statement.Variable statement) {
        KaoriType left = statement.type;
        KaoriType right = statement.right.acceptVisitor(this);

        if (left.equals(right)) {
            this.environment.set(statement.left, right);
        } else {
            throw KaoriError.TypeError("expected " + left + " type for " + statement.left.value, this.line);
        }
    }

    @Override
    public void visitExpressionStatement(Statement.Expr statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitIfStatement(Statement.If statement) {
        KaoriType condition = statement.condition.acceptVisitor(this);

        if (!condition.equals(KaoriType.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.thenBranch.acceptVisitor(this);
        statement.elseBranch.acceptVisitor(this);

    }

    @Override
    public void visitWhileLoopStatement(Statement.WhileLoop statement) {
        KaoriType condition = statement.condition.acceptVisitor(this);

        if (!condition.equals(KaoriType.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.block.acceptVisitor(this);
    }

    @Override
    public void visitForLoopStatement(Statement.ForLoop statement) {
        statement.variable.acceptVisitor(this);

        KaoriType condition = statement.condition.acceptVisitor(this);

        if (!condition.equals(KaoriType.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.block.acceptVisitor(this);
        statement.increment.acceptVisitor(this);
    }

    @Override
    public void visitFunctionStatement(Statement.Function statement) {
        // TODO Auto-generated method stub

    }

    @Override
    public KaoriType visitFunctionCall(FunctionCall node) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
    }

}
