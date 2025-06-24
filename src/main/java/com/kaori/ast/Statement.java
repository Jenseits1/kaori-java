package com.kaori.ast;

import java.util.List;

public abstract class Statement {
    public final int line;

    private Statement(int line) {
        this.line = line;
    }

    public abstract void acceptVisitor(Visitor visitor);

    public interface Visitor {
        void visitExpressionStatement(Expr statement);

        void visitPrintStatement(Print statement);

        void visitBlockStatement(Block statement);

        void visitVariableStatement(Variable statement);

        void visitIfStatement(If statement);

        void visitWhileLoopStatement(WhileLoop statement);

        void visitForLoopStatement(ForLoop statement);
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

    public static class Variable extends Statement {
        public final Expression.Identifier left;
        public final Expression right;

        public Variable(int line, Expression.Identifier left, Expression right) {
            super(line);
            this.left = left;
            this.right = right;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitVariableStatement(this);
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
            visitor.visitIfStatement(this);
        }

    }

    public static class WhileLoop extends Statement {
        public final Expression condition;
        public final Statement block;

        public WhileLoop(int line, Expression condition, Statement block) {
            super(line);
            this.condition = condition;
            this.block = block;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitWhileLoopStatement(this);
        }
    }

    public static class ForLoop extends Statement {
        public final Statement variable;
        public final Expression condition;
        public final Statement increment;
        public final Statement block;

        public ForLoop(int line, Statement variable, Expression condition, Statement increment, Statement block) {
            super(line);
            this.variable = variable;
            this.condition = condition;
            this.increment = increment;
            this.block = block;
        }

        @Override
        public void acceptVisitor(Visitor visitor) {
            visitor.visitForLoopStatement(this);
        }

    }
}
