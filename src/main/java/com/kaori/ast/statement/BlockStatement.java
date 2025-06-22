package com.kaori.ast.statement;

import java.util.List;

import com.kaori.interpreter.Visitor;

public class BlockStatement extends Statement {
    public final List<Statement> statements;

    public BlockStatement(int line, List<Statement> statements) {
        super(line);
        this.statements = statements;
    }

    @Override
    public void acceptVisitor(Visitor visitor) {
        visitor.visitBlockStatement(this);
    }

}
