package com.kaori.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kaori.ast.expression.Expression;
import com.kaori.ast.expression.literal.BooleanLiteral;
import com.kaori.ast.expression.literal.FloatLiteral;
import com.kaori.ast.expression.literal.StringLiteral;
import com.kaori.ast.expression.operators.binary.AddOperator;
import com.kaori.ast.expression.operators.binary.DivideOperator;
import com.kaori.ast.expression.operators.binary.ModuloOperator;
import com.kaori.ast.expression.operators.binary.MultiplyOperator;
import com.kaori.ast.expression.operators.binary.SubtractOperator;
import com.kaori.ast.expression.operators.unary.NegationOperator;
import com.kaori.ast.statement.ExpressionStatement;
import com.kaori.ast.statement.PrintStatement;
import com.kaori.ast.statement.Statement;
import com.kaori.lexer.Token;
import com.kaori.lexer.TokenType;

public class Parser {
    final List<Token> tokens;
    int currentIndex;
    int line;
    Token currentToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    boolean parseAtEnd() {
        return currentIndex >= tokens.size();
    }

    void consume(TokenType expected, String errorMessage) {
        if (parseAtEnd() || currentToken.type != expected) {
            throw new Error(errorMessage);
        }

        consume();
    }

    void consume() {
        currentIndex++;

        if (!parseAtEnd()) {
            currentToken = tokens.get(currentIndex);
            line = currentToken.line;
        }
    }

    Expression parenthesis() {
        consume(TokenType.LEFT_PAREN, "expected (");

        Expression expr = expression();

        consume(TokenType.RIGHT_PAREN, "expected )");

        return expr;
    }

    Expression primary() {
        if (parseAtEnd()) {
            throw new Error("expected valid operand");
        }

        return switch (currentToken.type) {
            case BOOLEAN_LITERAL -> {
                Expression literal = new BooleanLiteral(Boolean.parseBoolean(currentToken.lexeme));
                consume();
                yield literal;
            }
            case STRING_LITERAL -> {
                Expression literal = new StringLiteral(currentToken.lexeme);
                consume();
                yield literal;
            }
            case FLOAT_LITERAL -> {
                Expression literal = new FloatLiteral(Float.parseFloat(currentToken.lexeme));
                consume();
                yield literal;
            }
            case LEFT_PAREN -> parenthesis();
            default -> throw new Error("expected valid operand");
        };

    }

    Expression unary() {
        return switch (currentToken.type) {
            case MINUS -> {
                consume();
                yield new NegationOperator(unary());
            }
            case PLUS -> {
                consume();
                yield unary();

            }
            default -> primary();
        };
    }

    Expression factor() {
        Expression left = unary();

        while (!parseAtEnd()) {
            TokenType operator = currentToken.type;

            switch (operator) {
                case MULTIPLY -> {
                    consume();
                    Expression right = unary();
                    left = new MultiplyOperator(left, right);
                }
                case DIVIDE -> {
                    consume();
                    Expression right = unary();
                    left = new DivideOperator(left, right);
                }
                case MODULO -> {
                    consume();
                    Expression right = unary();
                    left = new ModuloOperator(left, right);
                }
                default -> {
                    return left;
                }

            }

        }

        return left;
    }

    Expression term() {
        Expression left = factor();

        while (!parseAtEnd()) {
            TokenType operator = currentToken.type;

            switch (operator) {
                case PLUS -> {
                    consume();
                    Expression right = factor();
                    left = new AddOperator(left, right);
                }
                case MINUS -> {
                    consume();
                    Expression right = factor();
                    left = new SubtractOperator(left, right);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    /*
     * Expression comparison() {
     * Expression left = term();
     * 
     * while (equal(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS,
     * TokenType.LESS_EQUAL)) {
     * TokenType operator = currentToken.type;
     * 
     * consume(operator, Optional.empty());
     * 
     * Expression right = term();
     * 
     * left = new BinaryOperator(left, right, operator);
     * }
     * 
     * return left;
     * }
     * 
     * Expression equality() {
     * Expression left = comparison();
     * 
     * while (equal(TokenType.EQUAL, TokenType.NOT_EQUAL)) {
     * TokenType operator = currentToken.type;
     * 
     * consume(operator, Optional.empty());
     * 
     * Expression right = comparison();
     * 
     * left = new BinaryOperator(left, right, operator);
     * }
     * 
     * return left;
     * }
     * 
     * Expression and() {
     * Expression left = equality();
     * 
     * while (equal(TokenType.AND)) {
     * TokenType operator = currentToken.type;
     * 
     * consume(operator, Optional.empty());
     * 
     * Expression right = equality();
     * 
     * left = new BinaryOperator(left, right, operator);
     * }
     * 
     * return left;
     * }
     * 
     * Expression or() {
     * Expression left = and();
     * 
     * while (equal(TokenType.OR)) {
     * TokenType operator = currentToken.type;
     * 
     * consume(operator, Optional.empty());
     * 
     * Expression right = and();
     * 
     * left = new BinaryOperator(left, right, operator);
     * }
     * 
     * return left;
     * }
     */

    Expression expression() {
        return term();
    }

    Statement expressionStatement() {
        Expression expression = expression();
        Statement statement = new ExpressionStatement(expression, line);

        consume(TokenType.SEMICOLON, "expected ; at the end of statement");

        return statement;
    }

    Statement printStatement() {
        consume(TokenType.PRINT, "expected print keyword");

        consume(TokenType.LEFT_PAREN, "expected (");

        Expression expression = expression();

        consume(TokenType.RIGHT_PAREN, "expected )");

        consume(TokenType.SEMICOLON, "expected ; at the end of statement");

        Statement statement = new PrintStatement(expression, line);

        return statement;
    }

    Statement statement() {
        return switch (currentToken.type) {
            case PRINT -> printStatement();
            default -> expressionStatement();
        };
    }

    void reset() {
        currentIndex = 0;
        currentToken = tokens.get(0);
        line = 1;
    }

    List<Statement> start() {
        List<Statement> statements = new ArrayList<>();

        while (!parseAtEnd()) {
            Statement statement = statement();

            statements.add(statement);
        }

        return statements;
    }

    public List<Statement> parse() {
        reset();
        List<Statement> statements = start();

        return statements;
    }
}
