package com.yellowflash.ast;

import com.yellowflash.lexer.TokenType;

public class BinaryOperator extends Expression {
    Expression leftOperand;
    Expression rightOperand;
    TokenType operator;

    public BinaryOperator(Expression leftOperand, Expression rightOperand, TokenType operator) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
    }
}
