package com.kaori.parser;

import java.util.List;

import com.kaori.visitor.Visitor;

public abstract class Expression {
    public abstract <T> T acceptVisitor(Visitor<T> visitor);

    public static class StringLiteral extends Expression {
        public final String value;

        public StringLiteral(String value) {
            this.value = value;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitStringLiteral(this);
        }
    }

    public static class NumberLiteral extends Expression {
        public final double value;

        public NumberLiteral(double value) {
            this.value = value;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitNumberLiteral(this);
        }
    }

    public static class BooleanLiteral extends Expression {
        public final boolean value;

        public BooleanLiteral(boolean value) {
            this.value = value;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitBooleanLiteral(this);
        }
    }

    public static class FunctionLiteral extends Expression {
        public final List<Statement> parameters;
        public final Statement block;

        public FunctionLiteral(List<Statement> parameters, Statement block) {
            this.parameters = parameters;
            this.block = block;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitFunctionLiteral(this);
        }
    }

    public static class Identifier extends Expression {
        public final String value;

        public Identifier(String value) {
            this.value = value;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitIdentifier(this);
        }
    }

    public static class Add extends Expression {
        public final Expression left;
        public final Expression right;

        public Add(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitAdd(this);
        }
    }

    public static class Subtract extends Expression {
        public final Expression left;
        public final Expression right;

        public Subtract(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitSubtract(this);
        }
    }

    public static class Multiply extends Expression {
        public final Expression left;
        public final Expression right;

        public Multiply(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitMultiply(this);
        }
    }

    public static class Divide extends Expression {
        public final Expression left;
        public final Expression right;

        public Divide(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitDivide(this);
        }
    }

    public static class Modulo extends Expression {
        public final Expression left;
        public final Expression right;

        public Modulo(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitModulo(this);
        }
    }

    public static class And extends Expression {
        public final Expression left;
        public final Expression right;

        public And(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitAnd(this);
        }
    }

    public static class Or extends Expression {
        public final Expression left;
        public final Expression right;

        public Or(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitOr(this);
        }
    }

    public static class Equal extends Expression {
        public final Expression left;
        public final Expression right;

        public Equal(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitEqual(this);
        }
    }

    public static class NotEqual extends Expression {
        public final Expression left;
        public final Expression right;

        public NotEqual(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitNotEqual(this);
        }
    }

    public static class Greater extends Expression {
        public final Expression left;
        public final Expression right;

        public Greater(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitGreater(this);
        }
    }

    public static class GreaterEqual extends Expression {
        public final Expression left;
        public final Expression right;

        public GreaterEqual(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitGreaterEqual(this);
        }
    }

    public static class Less extends Expression {
        public final Expression left;
        public final Expression right;

        public Less(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitLess(this);
        }
    }

    public static class LessEqual extends Expression {
        public final Expression left;
        public final Expression right;

        public LessEqual(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitLessEqual(this);
        }
    }

    public static class Assign extends Expression {
        public final Expression left;
        public final Expression right;

        public Assign(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitAssign(this);
        }
    }

    public static class Not extends Expression {
        public final Expression left;

        public Not(Expression left) {
            this.left = left;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitNot(this);
        }
    }

    public static class Negation extends Expression {
        public final Expression left;

        public Negation(Expression left) {
            this.left = left;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitNegation(this);
        }
    }
}
