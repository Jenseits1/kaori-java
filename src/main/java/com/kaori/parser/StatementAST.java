package com.kaori.parser;

import java.util.ArrayList;
import java.util.List;

public abstract class StatementAST {
    public final int line;

    private StatementAST(int line) {
        this.line = line;
    }

    public static class Print extends StatementAST {
        public final ExpressionAST expression;

        public Print(int line, ExpressionAST expression) {
            super(line);
            this.expression = expression;
        }
    }

    public static class Expr extends StatementAST {
        public final ExpressionAST expression;

        public Expr(int line, ExpressionAST expression) {
            super(line);
            this.expression = expression;
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
    }

    public static class WhileLoop extends StatementAST {
        public final ExpressionAST condition;
        public final Block block;

        public WhileLoop(int line, ExpressionAST condition, Block block) {
            super(line);
            this.condition = condition;
            this.block = block;
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
    }

    public static class Function extends StatementAST {
        public final ExpressionAST.Identifier name;
        public final List<Variable> parameters;
        public final TypeAST returnType;
        public final Block block;

        public Function(int line, ExpressionAST.Identifier name, List<Variable> parameters, TypeAST returnType,
                Block block) {
            super(line);
            this.name = name;
            this.parameters = parameters;
            this.returnType = returnType;
            this.block = block;
        }
    }

    public static class FunctionDecl extends StatementAST {
        public final ExpressionAST.Identifier name;
        public final List<Variable> parameters;
        public final TypeAST returnType;

        public FunctionDecl(int line, ExpressionAST.Identifier name, List<Variable> parameters, TypeAST returnType) {
            super(line);
            this.name = name;
            this.parameters = parameters;
            this.returnType = returnType;
        }
    }
}
