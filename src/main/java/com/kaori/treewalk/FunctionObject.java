package com.kaori.treewalk;

import java.util.List;

import com.kaori.parser.DeclarationAST;

public record FunctionObject(List<DeclarationAST.Variable> parameters, List<DeclarationAST> declarations) {

}
