package com.kaori.parser;

import java.util.List;
import com.kaori.visitor.Visitor;

public abstract class ExpressionAST {
    public abstract <T> T acceptVisitor(Visitor<T> visitor);

    public static enum Operator {
        // Arithmetic operators
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        MODULO("%"),

        // Unary arithmetic operators
        INCREMENT("++"),
        DECREMENT("--"),

        // Logical operators
        AND("&&"),
        OR("||"),
        NOT("!"),

        // Comparison
        NOT_EQUAL("!="),
        EQUAL("=="),
        GREATER(">"),
        GREATER_EQUAL(">="),
        LESS("<"),
        LESS_EQUAL("<="),

        ASSIGN("=");

        public final String label;

        private Operator(String label) {
            this.label = label;
        }

        public String toString() {
            return this.label;
        }
    }

    public static class Literal extends ExpressionAST {
        public final TypeAST type;
        public final Object value;

        public Literal(TypeAST type, Object value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class Identifier extends ExpressionAST {
        public final String value;

        public Identifier(String value) {
            this.value = value;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitIdentifier(this);
        }
    }

    public static class BinaryOperator extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;
        public final Operator operator;

        public BinaryOperator(ExpressionAST left, ExpressionAST right, Operator operator) {
            this.left = left;
            this.right = right;
            this.operator = operator;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitBinaryOperator(this);
        }
    }

    public static class Add extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public Add(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitAdd(this);
        }
    }

    public static class Subtract extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public Subtract(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitSubtract(this);
        }
    }

    public static class Multiply extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public Multiply(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitMultiply(this);
        }
    }

    public static class Divide extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public Divide(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitDivide(this);
        }
    }

    public static class Modulo extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public Modulo(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitModulo(this);
        }
    }

    public static class And extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public And(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitAnd(this);
        }
    }

    public static class Or extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public Or(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitOr(this);
        }
    }

    public static class Equal extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public Equal(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitEqual(this);
        }
    }

    public static class NotEqual extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public NotEqual(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitNotEqual(this);
        }
    }

    public static class Greater extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public Greater(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitGreater(this);
        }
    }

    public static class GreaterEqual extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public GreaterEqual(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitGreaterEqual(this);
        }
    }

    public static class Less extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public Less(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitLess(this);
        }
    }

    public static class LessEqual extends ExpressionAST {
        public final ExpressionAST left;
        public final ExpressionAST right;

        public LessEqual(ExpressionAST left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitLessEqual(this);
        }
    }

    public static class Assign extends ExpressionAST {
        public final Identifier left;
        public final ExpressionAST right;

        public Assign(Identifier left, ExpressionAST right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitAssign(this);
        }
    }

    public static class Not extends ExpressionAST {
        public final ExpressionAST left;

        public Not(ExpressionAST left) {
            this.left = left;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitNot(this);
        }
    }

    public static class Negation extends ExpressionAST {
        public final ExpressionAST left;

        public Negation(ExpressionAST left) {
            this.left = left;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitNegation(this);
        }
    }

    public static class FunctionCall extends ExpressionAST {
        public final ExpressionAST callee;
        public final List<ExpressionAST> arguments;

        public FunctionCall(ExpressionAST callee, List<ExpressionAST> arguments) {
            this.callee = callee;
            this.arguments = arguments;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitFunctionCall(this);
        }
    }
}
