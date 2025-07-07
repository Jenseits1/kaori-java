package com.kaori.parser;

import java.util.ArrayList;
import java.util.List;

import com.kaori.visitor.Visitor;

public abstract class StatementAST {
    public final int line;

    private StatementAST(int line) {
        this.line = line;
    }

    public abstract <T> void acceptVisitor(Visitor<T> visitor);

    public static class Print extends StatementAST {
        public final ExpressionAST expression;

        public Print(int line, ExpressionAST expression) {
            super(line);
            this.expression = expression;
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitPrintStatement(this);
        }
    }

    public static class Expr extends StatementAST {
        public final ExpressionAST expression;

        public Expr(int line, ExpressionAST expression) {
            super(line);
            this.expression = expression;
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitExpressionStatement(this);
        }
    }

    public static class Variable extends StatementAST {
        public final ExpressionAST.Identifier left;
        public final ExpressionAST right;
        public final TypeAST type;

        public Variable(int line, ExpressionAST.Identifier left, ExpressionAST right, TypeAST type) {
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

    public static class Block extends StatementAST {
        public final List<StatementAST> statements;

        public Block(int line, List<StatementAST> statements) {
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

    public static class If extends StatementAST {
        public final ExpressionAST condition;
        public final Block thenBranch;
        public final StatementAST elseBranch;

        public If(int line, ExpressionAST condition, Block thenBranch, StatementAST elseBranch) {
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

    public static class WhileLoop extends StatementAST {
        public final ExpressionAST condition;
        public final Block block;

        public WhileLoop(int line, ExpressionAST condition, Block block) {
            super(line);
            this.condition = condition;
            this.block = block;
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitWhileLoopStatement(this);
        }
    }

    public static class ForLoop extends StatementAST {
        public final Variable variable;
        public final ExpressionAST condition;
        public final ExpressionAST increment;
        public final Block block;

        public ForLoop(int line, Variable variable, ExpressionAST condition, ExpressionAST increment, Block block) {
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

    public static class Function extends StatementAST {
        public final ExpressionAST.Identifier name;
        public final List<Variable> parameters;
        public final Block block;

        public Function(int line, ExpressionAST.Identifier name, List<Variable> parameters, Block block) {
            super(line);
            this.name = name;
            this.parameters = parameters;
            this.block = block;
        }

        @Override
        public <T> void acceptVisitor(Visitor<T> visitor) {
            visitor.visitFunctionStatement(this);
        }
    }
}
