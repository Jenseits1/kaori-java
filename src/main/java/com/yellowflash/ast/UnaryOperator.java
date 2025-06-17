package com.yellowflash.ast;

import com.yellowflash.lexer.TokenType;

public class UnaryOperator {
    Expression operand;
    TokenType operator;

    public UnaryOperator(Expression operand, TokenType operator) {
        this.operand = operand;
        this.operator = operator;
    }
}
