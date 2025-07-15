package com.kaori.parser;

import java.util.List;

public interface ExpressionAST {
    public static enum Operator implements ExpressionAST {
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
        LESS_EQUAL("<=");

        public final String label;

        private Operator(String label) {
            this.label = label;
        }

        public String toString() {
            return this.label;
        }
    }

    record BinaryOperator(ExpressionAST left, ExpressionAST right, Operator operator) implements ExpressionAST {
    }

    record UnaryOperator(ExpressionAST left, Operator operator) implements ExpressionAST {
    }

    record Assign(Identifier left, ExpressionAST right) implements ExpressionAST {
    }

    record Literal(TypeAST type, Object value) implements ExpressionAST {
    }

    record Identifier(String value) implements ExpressionAST {
    }

    record FunctionCall(ExpressionAST callee, List<ExpressionAST> arguments) implements ExpressionAST {
    }
}
