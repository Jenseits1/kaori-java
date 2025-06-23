package com.kaori.ast;

import com.kaori.runtime.Visitor;

public interface Expression {
    public Object acceptVisitor(Visitor visitor);

    public static class Literal implements Expression {
        public final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public Object acceptVisitor(Visitor visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class Add implements Expression {
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

    public static class Subtract implements Expression {
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

    public static class Multiply implements Expression {
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

    public static class Divide implements Expression {
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

    public static class Modulo implements Expression {
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

    public static class And implements Expression {
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

    public static class Or implements Expression {
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

    public static class Equal implements Expression {
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

    public static class NotEqual implements Expression {
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

    public static class Greater implements Expression {
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

    public static class GreaterEqual implements Expression {
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

    public static class Less implements Expression {
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

    public static class LessEqual implements Expression {
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

    public static class Not implements Expression {
        public final Expression left;

        public Not(Expression left) {
            this.left = left;
        }

        @Override
        public Object acceptVisitor(Visitor visitor) {
            return visitor.visitNot(this);
        }
    }

    public static class Negation implements Expression {
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
