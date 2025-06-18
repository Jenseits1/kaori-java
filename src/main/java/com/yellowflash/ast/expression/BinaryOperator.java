package com.yellowflash.ast.expression;

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
