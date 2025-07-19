package com.kaori.ast;

import java.util.List;

public interface DeclarationAST {
    int line();

    public record Function(
            int line,
            ExpressionAST.Identifier name,
            List<Variable> parameters,
            TypeAST.Function type,
            StatementAST.Block block) implements DeclarationAST {
    }

    record Variable(
            int line,
            ExpressionAST.Identifier left,
            ExpressionAST right,
            TypeAST type) implements DeclarationAST {
    }
}
