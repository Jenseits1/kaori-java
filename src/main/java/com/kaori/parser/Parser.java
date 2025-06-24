package com.kaori.parser;

import java.util.ArrayList;
import java.util.List;
import com.kaori.ast.Expression;
import com.kaori.ast.Statement;
import com.kaori.error.KaoriError;
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
            throw KaoriError.SyntaxError(errorMessage, line);
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

        Expression expression = expression();

        consume(TokenType.RIGHT_PAREN, "expected )");

        return expression;
    }

    Expression primary() {
        if (parseAtEnd()) {
            throw KaoriError.SyntaxError("expected valid operand", line);
        }

        return switch (currentToken.type) {
            case BOOLEAN_LITERAL -> {
                boolean value = Boolean.parseBoolean(currentToken.lexeme);
                Expression literal = new Expression.Literal(value);
                consume();
                yield literal;
            }
            case STRING_LITERAL -> {
                String value = currentToken.lexeme.substring(1, currentToken.lexeme.length() - 1);
                Expression literal = new Expression.Literal(value);
                consume();
                yield literal;
            }
            case FLOAT_LITERAL -> {
                float value = Float.parseFloat(currentToken.lexeme);
                Expression literal = new Expression.Literal(value);
                consume();
                yield literal;
            }
            case IDENTIFIER -> {
                Expression identifier = new Expression.Identifier(currentToken.lexeme);
                consume();
                yield identifier;
            }
            case LEFT_PAREN -> parenthesis();
            default -> throw KaoriError.SyntaxError("expected valid operand", line);
        };

    }

    Expression unary() {
        return switch (currentToken.type) {
            case MINUS -> {
                consume();
                yield new Expression.Negation(unary());
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
                    left = new Expression.Multiply(left, right);
                }
                case DIVIDE -> {
                    consume();
                    Expression right = unary();
                    left = new Expression.Divide(left, right);
                }
                case MODULO -> {
                    consume();
                    Expression right = unary();
                    left = new Expression.Modulo(left, right);
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
                    left = new Expression.Add(left, right);
                }
                case MINUS -> {
                    consume();
                    Expression right = factor();
                    left = new Expression.Subtract(left, right);
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
                    left = new Expression.Greater(left, right);
                }
                case GREATER_EQUAL -> {
                    consume();
                    Expression right = term();
                    left = new Expression.GreaterEqual(left, right);
                }
                case LESS -> {
                    consume();
                    Expression right = term();
                    left = new Expression.Less(left, right);
                }
                case LESS_EQUAL -> {
                    consume();
                    Expression right = term();
                    left = new Expression.LessEqual(left, right);
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
                    left = new Expression.Equal(left, right);
                }
                case NOT_EQUAL -> {
                    consume();
                    Expression right = comparison();
                    left = new Expression.NotEqual(left, right);
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
                    left = new Expression.And(left, right);
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
                    left = new Expression.Or(left, right);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    Expression assign() {
        Expression expression = expression();

        if (expression instanceof Expression.Identifier left) {
            consume(TokenType.ASSIGN, "expected =");
            Expression right = expression();

            return new Expression.Assign(left, right);
        }

        return expression;
    }

    Expression expression() {
        return or();
    }

    Statement expressionStatement() {
        Expression expression = assign();
        Statement statement = new Statement.Expr(line, expression);

        return statement;
    }

    Statement printStatement() {
        consume(TokenType.PRINT, "expected print keyword");

        consume(TokenType.LEFT_PAREN, "expected (");

        Expression expression = expression();

        consume(TokenType.RIGHT_PAREN, "expected )");

        return new Statement.Print(line, expression);
    }

    Statement blockStatement() {
        consume(TokenType.LEFT_BRACE, "expected {");

        List<Statement> statements = new ArrayList<>();

        while (!parseAtEnd()) {
            if (currentToken.type == TokenType.RIGHT_BRACE) {
                break;
            }

            Statement statement = statement();
            statements.add(statement);
        }

        consume(TokenType.RIGHT_BRACE, "expected }");

        return new Statement.Block(line, statements);
    }

    Statement variableStatement() {
        TokenType type = currentToken.type;
        consume();

        Expression.Identifier left = new Expression.Identifier(currentToken.lexeme);

        consume(TokenType.IDENTIFIER, "expected an identifier");
        consume(TokenType.ASSIGN, "expected =");

        Expression right = expression();

        return switch (type) {
            case STRING_VARIABLE -> new Statement.StringVariable(line, left, right);
            case BOOLEAN_VARIABLE -> new Statement.BooleanVariable(line, left, right);
            case FLOAT_VARIABLE -> new Statement.FloatVariable(line, left, right);
            default -> throw KaoriError.SyntaxError("expected valid variable declaration token", line);
        };

    }

    Statement ifStatement() {
        consume(TokenType.IF, "expected if keyword");
        consume(TokenType.LEFT_PAREN, "expected (");

        Expression condition = expression();

        consume(TokenType.RIGHT_PAREN, "expected )");

        Statement ifBranch = blockStatement();

        if (currentToken.type != TokenType.ELSE) {
            return new Statement.If(line, condition, ifBranch, null);
        }

        consume(TokenType.ELSE, "expected else keyword");

        Statement elseBranch = switch (currentToken.type) {
            case IF -> ifStatement();
            default -> blockStatement();
        };

        return new Statement.If(line, condition, ifBranch, elseBranch);
    }

    Statement whileLoopStatement() {
        consume(TokenType.WHILE, "expected while keyword");
        consume(TokenType.LEFT_PAREN, "expected (");

        Expression condition = expression();

        consume(TokenType.RIGHT_PAREN, "expected )");

        Statement block = blockStatement();

        return new Statement.WhileLoop(line, condition, block);
    }

    Statement forLoopStatement() {
        consume(TokenType.FOR, "expected for keyword");

        consume(TokenType.LEFT_PAREN, "expected (");

        Statement variable = variableStatement();

        consume(TokenType.SEMICOLON, "expected semicolon after variable declaration");

        Expression condition = expression();

        consume(TokenType.SEMICOLON, "expected semicolon after condition");

        Statement increment = expressionStatement();

        consume(TokenType.RIGHT_PAREN, "expected )");

        Statement block = blockStatement();

        return new Statement.ForLoop(line, variable, condition, increment, block);
    }

    Statement statement() {
        Statement statement = switch (currentToken.type) {
            case PRINT -> printStatement();
            case LEFT_BRACE -> blockStatement();
            case STRING_VARIABLE, BOOLEAN_VARIABLE, FLOAT_VARIABLE -> variableStatement();
            case IF -> ifStatement();
            case WHILE -> whileLoopStatement();
            case FOR -> forLoopStatement();
            default -> expressionStatement();
        };

        if (statement instanceof Statement.Block) {
            return statement;
        }

        consume(TokenType.SEMICOLON, "expected ; at the end of statement");

        return statement;
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
