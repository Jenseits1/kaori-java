package com.kaori.memory;

import java.util.List;

import com.kaori.ast.DeclarationAST;
import com.kaori.ast.StatementAST;

public class Function {
    public final List<DeclarationAST.Variable> parameters;
    public final StatementAST.Block block;

    public Function(List<DeclarationAST.Variable> parameters, StatementAST.Block block) {
        this.parameters = parameters;
        this.block = block;
    }
}
