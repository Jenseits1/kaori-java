package com.kaori.visitor;

import java.util.List;

import com.kaori.parser.StatementAST;
import com.kaori.parser.TypeAST;

public class FunctionObject {
    public final List<StatementAST.Variable> parameters;
    public final StatementAST.Block block;
    public final TypeAST.Function type;

    public FunctionObject(List<StatementAST.Variable> parameters, StatementAST.Block block, TypeAST.Function type) {
        this.parameters = parameters;
        this.block = block;
        this.type = type;
    }
}
