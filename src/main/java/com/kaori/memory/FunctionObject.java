package com.kaori.memory;

import java.util.List;

import com.kaori.ast.StatementAST;

public record FunctionObject(List<StatementAST.Variable> parameters, List<StatementAST> statements) {

}
