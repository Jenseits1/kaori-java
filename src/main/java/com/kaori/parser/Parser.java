package com.kaori.parser;

import java.util.ArrayList;
import java.util.List;
import com.kaori.ast.expression.Expression;
import com.kaori.ast.expression.Literal;
import com.kaori.ast.expression.operators.binary.Add;
import com.kaori.ast.expression.operators.binary.And;
import com.kaori.ast.expression.operators.binary.Divide;
import com.kaori.ast.expression.operators.binary.Equal;
import com.kaori.ast.expression.operators.binary.Greater;
import com.kaori.ast.expression.operators.binary.GreaterEqual;
import com.kaori.ast.expression.operators.binary.Less;
import com.kaori.ast.expression.operators.binary.LessEqual;
import com.kaori.ast.expression.operators.binary.Modulo;
import com.kaori.ast.expression.operators.binary.Multiply;
import com.kaori.ast.expression.operators.binary.NotEqual;
import com.kaori.ast.expression.operators.binary.Or;
import com.kaori.ast.expression.operators.binary.Subtract;
import com.kaori.ast.expression.operators.unary.Negation;
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
                Expression literal = new Literal(Boolean.parseBoolean(currentToken.lexeme));
                consume();
                yield literal;
            }
            case STRING_LITERAL -> {
                Expression literal = new Literal(currentToken.lexeme);
                consume();
                yield literal;
            }
            case FLOAT_LITERAL -> {
                Expression literal = new Literal(Float.parseFloat(currentToken.lexeme));
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
                yield new Negation(unary());
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
                    left = new Multiply(left, right);
                }
                case DIVIDE -> {
                    consume();
                    Expression right = unary();
                    left = new Divide(left, right);
                }
                case MODULO -> {
                    consume();
                    Expression right = unary();
                    left = new Modulo(left, right);
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
                    left = new Add(left, right);
                }
                case MINUS -> {
                    consume();
                    Expression right = factor();
                    left = new Subtract(left, right);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    Expression comparison() {
        Expression left = term();

        while (!parseAtEnd()) {
            TokenType operator = currentToken.type;

            switch (operator) {
                case GREATER -> {
                    consume();
                    Expression right = term();
                    left = new Greater(left, right);
                }
                case GREATER_EQUAL -> {
                    consume();
                    Expression right = term();
                    left = new GreaterEqual(left, right);
                }
                case LESS -> {
                    consume();
                    Expression right = term();
                    left = new Less(left, right);
                }
                case LESS_EQUAL -> {
                    consume();
                    Expression right = term();
                    left = new LessEqual(left, right);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    Expression equality() {
        Expression left = comparison();

        while (!parseAtEnd()) {
            TokenType operator = currentToken.type;

            switch (operator) {
                case EQUAL -> {
                    consume();
                    Expression right = comparison();
                    left = new Equal(left, right);
                }
                case NOT_EQUAL -> {
                    consume();
                    Expression right = comparison();
                    left = new NotEqual(left, right);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    Expression and() {
        Expression left = equality();

        while (!parseAtEnd()) {
            TokenType operator = currentToken.type;

            switch (operator) {
                case AND -> {
                    consume();
                    Expression right = equality();
                    left = new And(left, right);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    Expression or() {
        Expression left = and();

        while (!parseAtEnd()) {
            TokenType operator = currentToken.type;

            switch (operator) {
                case OR -> {
                    consume();
                    Expression right = and();
                    left = new Or(left, right);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    Expression expression() {
        return or();
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
