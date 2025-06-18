package com.yellowflash.parser;

import java.util.List;
import java.util.Optional;

import com.yellowflash.ast.expression.BinaryOperator;
import com.yellowflash.ast.expression.BooleanLiteral;
import com.yellowflash.ast.expression.Expression;
import com.yellowflash.ast.expression.FloatLiteral;
import com.yellowflash.ast.expression.StringLiteral;
import com.yellowflash.ast.expression.UnaryOperator;
import com.yellowflash.lexer.Token;
import com.yellowflash.lexer.TokenType;

public class Parser {
    List<Token> tokens;
    int currentIndex;
    int line;
    Token currentToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentIndex = 0;
        this.currentToken = tokens.get(0);
        this.line = 1;
    }

    boolean parseAtEnd() {
        return currentIndex >= tokens.size();
    }

    void consume(TokenType expected, Optional<String> errorMessage) {
        if (parseAtEnd() || currentToken.type != expected) {
            throw new Error(errorMessage.get());
        }

        currentIndex++;

        if (!parseAtEnd()) {
            currentToken = tokens.get(currentIndex);
            line = currentToken.line;
        }
    }

    boolean match(TokenType... types) {
        if (parseAtEnd()) {
            return false;
        }

        for (TokenType type : types) {
            if (currentToken.type == type) {
                return true;
            }
        }

        return false;
    }

    Expression parenthesis() {
        consume(TokenType.LEFT_PAREN, Optional.empty());

        Expression expr = expression();

        consume(TokenType.RIGHT_PAREN, Optional.of("expected )"));

        return expr;
    }

    Expression primary() {
        if (parseAtEnd()) {
            throw new Error("expected valid operand");
        }

        Expression primary = switch (currentToken.type) {
            case BOOLEAN_LITERAL -> new BooleanLiteral(Boolean.parseBoolean(currentToken.lexeme));
            case STRING_LITERAL -> new StringLiteral(currentToken.lexeme);
            case FLOAT_LITERAL -> new FloatLiteral(Float.parseFloat(currentToken.lexeme));
            case LEFT_PAREN -> parenthesis();
            default -> throw new Error("expected valid operand");
        };

        consume(currentToken.type, Optional.empty());

        return primary;
    }

    Expression unary() {
        return switch (currentToken.type) {
            case MINUS -> {
                consume(currentToken.type, Optional.empty());
                yield new UnaryOperator(unary(), TokenType.MINUS);
            }
            case PLUS -> {
                consume(currentToken.type, Optional.empty());
                yield unary();
            }
            default -> primary();
        };
    }

    Expression factor() {
        Expression leftOperand = unary();

        while (match(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.REMAINDER)) {
            TokenType operator = currentToken.type;

            consume(operator, Optional.empty());

            Expression rightOperand = unary();

            leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
        }

        return leftOperand;
    }

    Expression term() {
        Expression leftOperand = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            TokenType operator = currentToken.type;

            consume(operator, Optional.empty());

            Expression rightOperand = factor();

            leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
        }

        return leftOperand;
    }

    Expression comparison() {
        Expression leftOperand = term();

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            TokenType operator = currentToken.type;

            consume(operator, Optional.empty());

            Expression rightOperand = term();

            leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
        }

        return leftOperand;
    }

    Expression equality() {
        Expression leftOperand = comparison();

        while (match(TokenType.EQUAL, TokenType.NOT_EQUAL)) {
            TokenType operator = currentToken.type;

            consume(operator, Optional.empty());

            Expression rightOperand = comparison();

            leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
        }

        return leftOperand;
    }

    Expression and() {
        Expression leftOperand = equality();

        while (match(TokenType.AND)) {
            TokenType operator = currentToken.type;

            consume(operator, Optional.empty());

            Expression rightOperand = equality();

            leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
        }

        return leftOperand;
    }

    Expression or() {
        Expression leftOperand = and();

        while (match(TokenType.OR)) {
            TokenType operator = currentToken.type;

            consume(operator, Optional.empty());

            Expression rightOperand = and();

            leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
        }

        return leftOperand;
    }

    Expression expression() {
        return term();
    }

    public Expression execute() {
        return expression();
    }
}
