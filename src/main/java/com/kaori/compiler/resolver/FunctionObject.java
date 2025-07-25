package com.kaori.compiler.resolver;

import java.util.List;

import com.kaori.ast.DeclarationAST;

public record FunctionObject(List<DeclarationAST.Variable> parameters, List<DeclarationAST> declarations) {

}
