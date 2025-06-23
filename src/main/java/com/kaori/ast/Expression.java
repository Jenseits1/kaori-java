package com.kaori.ast;

public abstract class Expression {
    public abstract <T> T acceptVisitor(Visitor<T> visitor);

    public interface Visitor<T> {
        T visitLiteral(Literal node);

        T visitAdd(Add node);

        T visitSubtract(Subtract node);

        T visitMultiply(Multiply node);

        T visitDivide(Divide node);

        T visitModulo(Modulo node);

        T visitAnd(And node);

        T visitOr(Or node);

        T visitEqual(Equal node);

        T visitNotEqual(NotEqual node);

        T visitGreater(Greater node);

        T visitGreaterEqual(GreaterEqual node);

        T visitLess(Less node);

        T visitLessEqual(LessEqual node);

        T visitNegation(Negation node);

        T visitNot(Not node);
    }

    public static class Literal extends Expression {
        public final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
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
