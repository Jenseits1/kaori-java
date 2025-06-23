package com.kaori.ast;

import java.util.List;

public abstract class Statement {
    public final int line;

    private Statement(int line) {
        this.line = line;
    }

    public abstract void acceptVisitor(Visitor visitor);

    public interface Visitor {
        void visitExpressionStatement(Expr node);

        void visitPrintStatement(Print node);

        void visitBlockStatement(Block node);

        void visitFloatVariableStatement(FloatVariable floatVariable);

        void visitBooleanVariableStatement(BooleanVariable booleanVariable);

        void visitStringVariableStatement(StringVariable stringVariable);
    }

    public static class Print extends Statement {

        public final Expression expression;

        public Print(int line, Expression expression) {
            super(line);
            this.expression = expression;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitPrintStatement(this);
        }
    }

    public static class Expr extends Statement {
        public final Expression expression;

        public Expr(int line, Expression expression) {
            super(line);
            this.expression = expression;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitExpressionStatement(this);
        }
    }

    public static class Block extends Statement {
        public final List<Statement> statements;

        public Block(int line, List<Statement> statements) {
            super(line);
            this.statements = statements;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitBlockStatement(this);
        }
    }

    public static class StringVariable extends Statement {
        public final String identifier;
        public final Expression value;

        public StringVariable(int line, String identifier, Expression value) {
            super(line);
            this.identifier = identifier;
            this.value = value;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitStringVariableStatement(this);
        }
    }

    public static class BooleanVariable extends Statement {
        public final String identifier;
        public final Expression value;

        public BooleanVariable(int line, String identifier, Expression value) {
            super(line);
            this.identifier = identifier;
            this.value = value;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitBooleanVariableStatement(this);
        }
    }

    public static class FloatVariable extends Statement {
        public final String identifier;
        public final Expression value;

        public FloatVariable(int line, String identifier, Expression value) {
            super(line);
            this.identifier = identifier;
            this.value = value;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitFloatVariableStatement(this);
        }

    }
}
