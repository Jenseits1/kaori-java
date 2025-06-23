package com.kaori.ast;

import java.util.List;

public abstract class Statement {
    public final int line;

    public Statement(int line) {
        this.line = line;
    }

    public abstract void acceptVisitor(Visitor visitor);

    public interface Visitor {
        void visitExpressionStatement(Statement.Expr node);

        void visitPrintStatement(Statement.Print node);

        void visitBlockStatement(Statement.Block node);
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

}
