package com.kaori.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kaori.ast.expression.BooleanLiteral;
import com.kaori.ast.expression.Expression;
import com.kaori.ast.expression.FloatLiteral;
import com.kaori.ast.expression.StringLiteral;
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

    boolean equal(TokenType... types) {
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

                yield new NegationOperator(unary());
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

        while (!parseAtEnd()) {
            TokenType operator = currentToken.type;

            switch (operator) {
                case MULTIPLY:
                    consume(operator, Optional.empty());
                    leftOperand = new MultiplyOperator(leftOperand, unary());
                    break;
                case DIVIDE:
                    consume(operator, Optional.empty());
                    leftOperand = new DivideOperator(leftOperand, unary());
                    break;
                case MODULO:
                    consume(operator, Optional.empty());
                    leftOperand = new ModuloOperator(leftOperand, unary());
                    break;
                default:
                    return leftOperand;
            }

        }

        return leftOperand;
    }

    Expression term() {
        Expression leftOperand = factor();

        while (!parseAtEnd()) {
            TokenType operator = currentToken.type;

            switch (operator) {
                case PLUS:
                    consume(operator, Optional.empty());
                    leftOperand = new AddOperator(leftOperand, factor());
                    break;
                case MINUS:
                    consume(operator, Optional.empty());
                    leftOperand = new SubtractOperator(leftOperand, factor());
                    break;
                default:
                    return leftOperand;
            }

        }

        return leftOperand;
    }
    /*
     * Expression comparison() {
     * Expression leftOperand = term();
     * 
     * while (equal(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS,
     * TokenType.LESS_EQUAL)) {
     * TokenType operator = currentToken.type;
     * 
     * consume(operator, Optional.empty());
     * 
     * Expression rightOperand = term();
     * 
     * leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
     * }
     * 
     * return leftOperand;
     * }
     * 
     * Expression equality() {
     * Expression leftOperand = comparison();
     * 
     * while (equal(TokenType.EQUAL, TokenType.NOT_EQUAL)) {
     * TokenType operator = currentToken.type;
     * 
     * consume(operator, Optional.empty());
     * 
     * Expression rightOperand = comparison();
     * 
     * leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
     * }
     * 
     * return leftOperand;
     * }
     * 
     * Expression and() {
     * Expression leftOperand = equality();
     * 
     * while (equal(TokenType.AND)) {
     * TokenType operator = currentToken.type;
     * 
     * consume(operator, Optional.empty());
     * 
     * Expression rightOperand = equality();
     * 
     * leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
     * }
     * 
     * return leftOperand;
     * }
     * 
     * Expression or() {
     * Expression leftOperand = and();
     * 
     * while (equal(TokenType.OR)) {
     * TokenType operator = currentToken.type;
     * 
     * consume(operator, Optional.empty());
     * 
     * Expression rightOperand = and();
     * 
     * leftOperand = new BinaryOperator(leftOperand, rightOperand, operator);
     * }
     * 
     * return leftOperand;
     * }
     */

    Expression expression() {
        return term();
    }

    Statement expressionStatement() {
        Expression expression = expression();
        Statement statement = new ExpressionStatement(expression, line);

        consume(TokenType.SEMICOLON, Optional.of("expected ; at the end of statement"));

        return statement;
    }

    Statement printStatement() {
        consume(TokenType.PRINT, Optional.empty());

        consume(TokenType.LEFT_PAREN, Optional.of("expected ("));

        Expression expression = expression();

        consume(TokenType.RIGHT_PAREN, Optional.of("expected )"));

        consume(TokenType.SEMICOLON, Optional.of("expected ; at the end of statement"));

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
