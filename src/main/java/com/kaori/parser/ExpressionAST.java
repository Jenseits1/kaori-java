package com.kaori.parser;

import java.util.List;
import com.kaori.visitor.Visitor;

public abstract class ExpressionAST {
    public abstract <T> T acceptVisitor(Visitor<T> visitor);

    public static enum Operator {
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        MODULO("%"),
        AND("&&"),
        OR("||"),
        NOT("!"),
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

    public static class UnaryOperator extends ExpressionAST {
        public final ExpressionAST left;
        public final Operator operator;

        public UnaryOperator(ExpressionAST left, Operator operator) {
            this.left = left;
            this.operator = operator;
        }

        @Override
        public <T> T acceptVisitor(Visitor<T> visitor) {
            return visitor.visitUnaryOperator(this);
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
