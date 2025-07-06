package com.kaori.parser;

import java.util.ArrayList;
import java.util.List;

import com.kaori.visitor.Visitor;

public abstract class Statement {
    public final int line;

    private Statement(int line) {
        this.line = 0;
    }

    public abstract <T> void acceptVisitor(Visitor<T> visitor);

    public static class Print extends Statement {
        public final Expression expression;

        public Print(int line, Expression expression) {
            super(line);
            this.expression = expression;
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
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
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitExpressionStatement(this);
        }
    }

    public static class Variable extends Statement {
        public final Expression.Identifier left;
        public final Expression right;
        public final KaoriType type;

        public Variable(int line, Expression.Identifier left, Expression right, KaoriType type) {
            super(line);
            this.left = left;
            this.right = right;
            this.type = type;
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitVariableStatement(this);
        }

    }

    public static class Block extends Statement {
        public final List<Statement> statements;

        public Block(int line, List<Statement> statements) {
            super(line);
            this.statements = statements;
        }

        public Block(int line) {
            super(line);
            this.statements = new ArrayList<>();
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitBlockStatement(this);
        }
    }

    public static class If extends Statement {
        public final Expression condition;
        public final Statement.Block thenBranch;
        public final Statement elseBranch;

        public If(int line, Expression condition, Statement.Block thenBranch, Statement elseBranch) {
            super(line);
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitIfStatement(this);
        }

    }

    public static class WhileLoop extends Statement {
        public final Expression condition;
        public final Statement.Block block;

        public WhileLoop(int line, Expression condition, Statement.Block block) {
            super(line);
            this.condition = condition;
            this.block = block;
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitWhileLoopStatement(this);
        }
    }

    public static class ForLoop extends Statement {
        public final Statement.Variable variable;
        public final Expression condition;
        public final Expression increment;
        public final Statement.Block block;

        public ForLoop(int line, Statement.Variable variable, Expression condition, Expression increment,
                Statement.Block block) {
            super(line);
            this.variable = variable;
            this.condition = condition;
            this.increment = increment;
            this.block = block;
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitForLoopStatement(this);
        }
    }
}
