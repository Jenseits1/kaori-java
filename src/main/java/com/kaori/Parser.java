package com.kaori;

import java.util.ArrayList;
import java.util.List;
import com.kaori.ast.Expression;
import com.kaori.ast.Statement;
import com.kaori.token.Token;
import com.kaori.token.TokenKind;

public class Parser {
    private final String source;
    private final List<Token> tokens;
    private int currentIndex;
    private int line;
    private Token currentToken;

    public Parser(String source, List<Token> tokens) {
        this.source = source;
        this.tokens = tokens;
    }

    private boolean parseAtEnd() {
        return currentIndex >= tokens.size();
    }

    private void consume(TokenKind expected, String errorMessage) {
        if (parseAtEnd() || currentToken.type != expected) {
            throw KaoriError.SyntaxError(errorMessage, line);
        }

        consume();
    }

    private void consume() {
        currentIndex++;

        if (!parseAtEnd()) {
            currentToken = tokens.get(currentIndex);
            this.line = currentToken.getLine();
        }
    }

    private Expression parenthesis() {
        consume(TokenKind.LEFT_PAREN, "expected (");

        Expression expression = expression();

        consume(TokenKind.RIGHT_PAREN, "expected )");

        return expression;
    }

    private Expression primary() {
        if (parseAtEnd()) {
            throw KaoriError.SyntaxError("expected valid operand", line);
        }

        return switch (currentToken.type) {
            case BOOLEAN_LITERAL -> {
                boolean value = Boolean.parseBoolean(currentToken.lexeme(source));
                Expression literal = new Expression.Literal(value);
                consume();
                yield literal;
            }
            case STRING_LITERAL -> {
                String value = currentToken.lexeme(source);
                Expression literal = new Expression.Literal(value.substring(1, value.length() - 1));
                consume();
                yield literal;
            }
            case FLOAT_LITERAL -> {
                float value = Float.parseFloat(currentToken.lexeme(source));
                Expression literal = new Expression.Literal(value);
                consume();
                yield literal;
            }
            case IDENTIFIER -> {
                Expression identifier = new Expression.Identifier(currentToken.lexeme(source));
                consume();
                yield identifier;
            }
            case LEFT_PAREN -> parenthesis();
            default -> throw KaoriError.SyntaxError("expected valid operand", line);
        };

    }

    private Expression unary() {
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

    private Expression factor() {
        Expression left = unary();

        while (!parseAtEnd()) {
            TokenKind operator = currentToken.type;

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

    private Expression term() {
        Expression left = factor();

        while (!parseAtEnd()) {
            TokenKind operator = currentToken.type;

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

    private Expression comparison() {
        Expression left = term();

        while (!parseAtEnd()) {
            TokenKind operator = currentToken.type;

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

    private Expression equality() {
        Expression left = comparison();

        while (!parseAtEnd()) {
            TokenKind operator = currentToken.type;

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

    private Expression and() {
        Expression left = equality();

        while (!parseAtEnd()) {
            TokenKind operator = currentToken.type;

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

    private Expression or() {
        Expression left = and();

        while (!parseAtEnd()) {
            TokenKind operator = currentToken.type;

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

    private Expression assign() {
        Expression expression = expression();

        if (expression instanceof Expression.Identifier left) {
            consume(TokenKind.ASSIGN, "expected =");
            Expression right = expression();

            return new Expression.Assign(left, right);
        }

        return expression;
    }

    private Expression expression() {
        return or();
    }

    private Statement expressionStatement() {
        Expression expression = assign();
        Statement statement = new Statement.Expr(expression);

        return statement;
    }

    private Statement printStatement() {
        consume(TokenKind.PRINT, "expected print keyword");

        consume(TokenKind.LEFT_PAREN, "expected (");

        Expression expression = expression();

        consume(TokenKind.RIGHT_PAREN, "expected )");

        return new Statement.Print(expression);
    }

    private Statement blockStatement() {
        consume(TokenKind.LEFT_BRACE, "expected {");

        List<Statement> statements = new ArrayList<>();

        while (!parseAtEnd() && currentToken.type != TokenKind.RIGHT_BRACE) {
            Statement statement = statement();
            statements.add(statement);
        }

        consume(TokenKind.RIGHT_BRACE, "expected }");

        return new Statement.Block(statements);
    }

    private Statement variableStatement() {
        consume(TokenKind.VARIABLE, "expected variable declaration keyword");

        Expression.Identifier left = new Expression.Identifier(currentToken.lexeme(source));

        consume(TokenKind.IDENTIFIER, "expected an identifier");
        consume(TokenKind.ASSIGN, "expected =");

        Expression right = expression();

        return new Statement.Variable(left, right);

    }

    private Statement ifStatement() {
        consume(TokenKind.IF, "expected if keyword");
        consume(TokenKind.LEFT_PAREN, "expected (");

        Expression condition = expression();

        consume(TokenKind.RIGHT_PAREN, "expected )");

        Statement ifBranch = blockStatement();

        if (currentToken.type != TokenKind.ELSE) {
            return new Statement.If(condition, ifBranch, null);
        }

        consume(TokenKind.ELSE, "expected else keyword");

        Statement elseBranch = switch (currentToken.type) {
            case IF -> ifStatement();
            default -> blockStatement();
        };

        return new Statement.If(condition, ifBranch, elseBranch);
    }

    private Statement whileLoopStatement() {
        consume(TokenKind.WHILE, "expected while keyword");
        consume(TokenKind.LEFT_PAREN, "expected (");

        Expression condition = expression();

        consume(TokenKind.RIGHT_PAREN, "expected )");

        Statement block = blockStatement();

        return new Statement.WhileLoop(condition, block);
    }

    private Statement forLoopStatement() {
        consume(TokenKind.FOR, "expected for keyword");

        consume(TokenKind.LEFT_PAREN, "expected (");

        Statement variable = variableStatement();

        consume(TokenKind.SEMICOLON, "expected semicolon after variable declaration");

        Expression condition = expression();

        consume(TokenKind.SEMICOLON, "expected semicolon after condition");

        Statement increment = expressionStatement();

        consume(TokenKind.RIGHT_PAREN, "expected )");

        Statement block = blockStatement();

        return new Statement.ForLoop(variable, condition, increment, block);
    }

    private Statement statement() {
        int currentStatementLine = currentToken.getLine();

        Statement statement = switch (currentToken.type) {
            case PRINT -> printStatement();
            case LEFT_BRACE -> blockStatement();
            case VARIABLE -> variableStatement();
            case IF -> ifStatement();
            case WHILE -> whileLoopStatement();
            case FOR -> forLoopStatement();
            default -> expressionStatement();
        };

        statement.setLine(currentStatementLine);

        if (statement instanceof Statement.Block) {
            return statement;
        }

        consume(TokenKind.SEMICOLON, "expected ; at the end of statement");

        return statement;
    }

    private void reset() {
        currentIndex = 0;
        currentToken = tokens.get(0);
        line = 1;
    }

    private List<Statement> start() {
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
