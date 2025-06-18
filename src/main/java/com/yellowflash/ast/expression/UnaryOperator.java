package com.yellowflash.ast.expression;

import com.yellowflash.lexer.TokenType;

public class UnaryOperator extends Expression {
    Expression operand;
    TokenType operator;

    public UnaryOperator(Expression operand, TokenType operator) {
        this.operand = operand;
        this.operator = operator;
    }
}
