package com.kaori.ast;

import java.util.ArrayList;
import java.util.List;

public interface StatementAST {
        int line();

        record Print(int line, ExpressionAST expression) implements StatementAST {
        }

        record Expr(int line, ExpressionAST expression) implements StatementAST {
        }

        record Variable(
                        int line,
                        ExpressionAST.Identifier left,
                        ExpressionAST right,
                        TypeAST type) implements StatementAST {
        }

        record Block(int line, List<StatementAST> statements) implements StatementAST {
                public Block(int line) {
                        this(line, new ArrayList<>());
                }
        }

        record If(
                        int line,
                        ExpressionAST condition,
                        Block thenBranch,
                        StatementAST elseBranch) implements StatementAST {
        }

        record WhileLoop(
                        int line,
                        ExpressionAST condition,
                        Block block) implements StatementAST {
        }

        record ForLoop(
                        int line,
                        Variable variable,
                        ExpressionAST condition,
                        ExpressionAST increment,
                        Block block) implements StatementAST {
        }

        record Function(
                        int line,
                        ExpressionAST.Identifier name,
                        List<Variable> parameters,
                        TypeAST.Function type,
                        Block block) implements StatementAST {
        }

        record FunctionDecl(
                        int line,
                        ExpressionAST.Identifier name,
                        List<Variable> parameters,
                        TypeAST.Function type) implements StatementAST {
        }
}
