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

        void visitIfStatement(If if1);
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
        public final Expression.Identifier left;
        public final Expression right;

        public StringVariable(int line, Expression.Identifier left, Expression right) {
            super(line);
            this.left = left;
            this.right = right;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitStringVariableStatement(this);
        }
    }

    public static class BooleanVariable extends Statement {
        public final Expression.Identifier left;
        public final Expression right;

        public BooleanVariable(int line, Expression.Identifier left, Expression right) {
            super(line);
            this.left = left;
            this.right = right;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitBooleanVariableStatement(this);
        }
    }

    public static class FloatVariable extends Statement {
        public final Expression.Identifier left;
        public final Expression right;

        public FloatVariable(int line, Expression.Identifier left, Expression right) {
            super(line);
            this.left = left;
            this.right = right;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitFloatVariableStatement(this);
        }

    }

    public static class If extends Statement {
        public final Expression condition;
        public final Statement ifBranch;
        public final Statement elseBranch;

        public If(int line, Expression condition, Statement ifBranch, Statement elseBranch) {
            super(line);
            this.condition = condition;
            this.ifBranch = ifBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            // TODO Auto-generated method stub
            visitor.visitIfStatement(this);
        }

    }
}
