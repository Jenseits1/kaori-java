package com.kaori.ast.expression.operators.binary;

import com.kaori.ast.expression.Expression;
import com.kaori.interpreter.Interpreter;

public class ModuloOperator extends BinaryOperator {
    public ModuloOperator(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Object acceptVisitor(Interpreter interpreter) {
        return interpreter.visitModuloOperator(this);
    }
}
