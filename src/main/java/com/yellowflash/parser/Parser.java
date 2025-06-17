package com.yellowflash.parser;

import java.util.List;
import java.util.Optional;

import com.yellowflash.ast.BinaryOperator;
import com.yellowflash.ast.BooleanLiteral;
import com.yellowflash.ast.Expression;
import com.yellowflash.ast.FloatLiteral;
import com.yellowflash.ast.StringLiteral;
import com.yellowflash.lexer.Token;
import com.yellowflash.lexer.TokenType;

public class Parser {
    List<Token> tokens;
    int current;
    int line;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
        this.line = 1;
    }

    Optional<Token> lookAhead() {
        if (current < tokens.size()) {
            return Optional.of(tokens.get(current));
        }

        return Optional.empty();
    }

    void consume(TokenType expected) {
        this.current++;
    }

    Expression parenthesis() {
        consume(TokenType.LEFT_PAREN);

        Expression expr = expression();

        consume(TokenType.RIGHT_PAREN);

        return expr;
    }

    Expression primary() {
        if (lookAhead().isEmpty()) {
            throw new Error("Expected a literal value token");
        }

        Token token = lookAhead().get();

        Expression primary = switch (token.type) {
            case BOOLEAN_LITERAL -> new BooleanLiteral(Boolean.parseBoolean(token.lexeme));
            case STRING_LITERAL -> new StringLiteral(token.lexeme);
            case FLOAT_LITERAL -> new FloatLiteral(Float.parseFloat(token.lexeme));
            case LEFT_PAREN -> parenthesis();
            default -> throw new Error("Expected a literal value token");
        };

        consume(token.type);

        return primary;
    }

    Expression factor() {
        Expression leftOperand = primary();

        while (lookAhead().isPresent()) {
            Token token = lookAhead().get();

            if (token.type != TokenType.MULTIPLY && token.type != TokenType.DIVIDE
                    && token.type != TokenType.REMAINDER) {
                break;
            }

            consume(token.type);

            Expression rightOperand = primary();

            leftOperand = new BinaryOperator(leftOperand, rightOperand, token.type);
        }

        return leftOperand;
    }

    Expression term() {
        Expression leftOperand = factor();

        while (lookAhead().isPresent()) {
            Token token = lookAhead().get();

            if (token.type != TokenType.PLUS && token.type != TokenType.MINUS) {
                break;
            }

            consume(token.type);

            Expression rightOperand = factor();

            leftOperand = new BinaryOperator(leftOperand, rightOperand, token.type);
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
