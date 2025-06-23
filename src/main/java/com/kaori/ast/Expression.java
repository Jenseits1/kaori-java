package com.kaori.ast;

public abstract class Expression {
    public abstract Object acceptVisitor(Visitor visitor);

    public interface Visitor {
        Object visitLiteral(Literal node);

        Object visitAdd(Add node);

        Object visitSubtract(Subtract node);

        Object visitMultiply(Multiply node);

        Object visitDivide(Divide node);

        Object visitModulo(Modulo node);

        Object visitAnd(And node);

        Object visitOr(Or node);

        Object visitEqual(Equal node);

        Object visitNotEqual(NotEqual node);

        Object visitGreater(Greater node);

        Object visitGreaterEqual(GreaterEqual node);

        Object visitLess(Less node);

        Object visitLessEqual(LessEqual node);

        Object visitNegation(Negation node);

        Object visitNot(Not node);
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
