package com.kaori.ast;

public abstract class Expression {
    public abstract Object acceptVisitor(Visitor visitor);

    public interface Visitor {
        Object visitLiteral(Expression.Literal node);

        Object visitAdd(Expression.Add node);

        Object visitSubtract(Expression.Subtract node);

        Object visitMultiply(Expression.Multiply node);

        Object visitDivide(Expression.Divide node);

        Object visitModulo(Expression.Modulo node);

        Object visitAnd(Expression.And node);

        Object visitOr(Expression.Or node);

        Object visitEqual(Expression.Equal node);

        Object visitNotEqual(Expression.NotEqual node);

        Object visitGreater(Expression.Greater node);

        Object visitGreaterEqual(Expression.GreaterEqual node);

        Object visitLess(Expression.Less node);

        Object visitLessEqual(Expression.LessEqual node);

        Object visitNegation(Expression.Negation node);

        Object visitNot(Expression.Not node);
    }

    public static class Literal extends Expression {
        public final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public Object acceptVisitor(Visitor visitor) {
            return visitor.visitLiteral(this);
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
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
        public Object acceptVisitor(Visitor visitor) {
            return visitor.visitLessEqual(this);
        }
    }

    public static class Not extends Expression {
        public final Expression left;

        public Not(Expression left) {
            this.left = left;
        }

        @Override
        public Object acceptVisitor(Visitor visitor) {
            return visitor.visitNot(this);
        }
    }

    public static class Negation extends Expression {
        public final Expression left;

        public Negation(Expression left) {
            this.left = left;
        }

        @Override
        public Object acceptVisitor(Visitor visitor) {
            return visitor.visitNegation(this);
        }
    }
}
