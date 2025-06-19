package com.kaori.interpreter;

import java.util.List;

import com.kaori.ast.statement.Statement;

public class Interpreter {
    List<Statement> statements;

    public Interpreter(List<Statement> statements) {
        this.statements = statements;
    }
}
