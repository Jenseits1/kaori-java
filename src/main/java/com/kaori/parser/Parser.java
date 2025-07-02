package com.kaori.parser;

import java.util.ArrayList;
import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.lexer.Token;
import com.kaori.lexer.TokenKind;

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
        return this.currentIndex >= this.tokens.size();
    }

    private void consume(TokenKind expected, String errorMessage) {
        if (this.parseAtEnd() || this.currentToken.type != expected) {
            throw KaoriError.SyntaxError(errorMessage, this.line);
        }

        this.consume();
    }

    private void consume() {
        this.currentIndex++;

        if (!this.parseAtEnd()) {
            this.currentToken = this.tokens.get(this.currentIndex);
            this.line = this.currentToken.getLine();
        }
    }

    private Expression postfixUnary() {
        throw KaoriError.SyntaxError("not found", this.line);
    }

    private Expression parenthesis() {
        this.consume(TokenKind.LEFT_PAREN, "expected (");

        Expression expression = expression();

        this.consume(TokenKind.RIGHT_PAREN, "expected )");

        return expression;
    }

    private Expression functionLiteral() {
        this.consume(TokenKind.FUNCTION, "expected function keyword");

        this.consume(TokenKind.LEFT_PAREN, "expected (");

        Expression parameters = this.expression();

        this.consume(TokenKind.RIGHT_PAREN, "expected )");
        Statement block = this.blockStatement();

        return new Expression.FunctionLiteral(parameters, block);
    }

    private Expression identifier() {
        String value = this.currentToken.lexeme(this.source);
        Expression identifier = new Expression.Identifier(value);
        this.consume();

        return identifier;
    }

    private Expression primary() {
        if (this.parseAtEnd()) {
            throw KaoriError.SyntaxError("expected valid operand", this.line);
        }

        return switch (this.currentToken.type) {
            case BOOLEAN_LITERAL -> {
                boolean value = Boolean.parseBoolean(this.currentToken.lexeme(this.source));
                Expression literal = new Expression.BooleanLiteral(value);
                consume();

                yield literal;
            }
            case STRING_LITERAL -> {
                String value = this.currentToken.lexeme(this.source);
                Expression literal = new Expression.StringLiteral(value.substring(1, value.length() - 1));
                this.consume();

                yield literal;
            }
            case NUMBER_LITERAL -> {
                float value = Float.parseFloat(this.currentToken.lexeme(this.source));
                Expression literal = new Expression.NumberLiteral(value);
                this.consume();

                yield literal;
            }
            case IDENTIFIER -> this.identifier();
            case FUNCTION -> this.functionLiteral();
            case LEFT_PAREN -> this.parenthesis();
            default -> throw KaoriError.SyntaxError("expected valid operand", this.line);
        };

    }

    private Expression prefixUnary() {
        return switch (this.currentToken.type) {
            case MINUS -> {
                this.consume();
                yield new Expression.Negation(this.prefixUnary());
            }
            case NOT -> {
                this.consume();
                yield new Expression.Not(this.prefixUnary());
            }
            case PLUS -> {
                this.consume();

                yield this.prefixUnary();

            }
            default -> this.primary();
        };
    }

    private Expression factor() {
        Expression left = this.prefixUnary();

        while (!this.parseAtEnd()) {
            TokenKind operator = this.currentToken.type;

            switch (operator) {
                case MULTIPLY -> {
                    this.consume();
                    Expression right = this.prefixUnary();
                    left = new Expression.Multiply(left, right);
                }
                case DIVIDE -> {
                    this.consume();
                    Expression right = this.prefixUnary();
                    left = new Expression.Divide(left, right);
                }
                case MODULO -> {
                    this.consume();
                    Expression right = this.prefixUnary();
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
        Expression left = this.factor();

        while (!this.parseAtEnd()) {
            TokenKind operator = this.currentToken.type;

            switch (operator) {
                case PLUS -> {
                    this.consume();
                    Expression right = this.factor();
                    left = new Expression.Add(left, right);
                }
                case MINUS -> {
                    this.consume();
                    Expression right = this.factor();
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
        Expression left = this.term();

        while (!this.parseAtEnd()) {
            TokenKind operator = this.currentToken.type;

            switch (operator) {
                case GREATER -> {
                    this.consume();
                    Expression right = this.term();
                    left = new Expression.Greater(left, right);
                }
                case GREATER_EQUAL -> {
                    this.consume();
                    Expression right = this.term();
                    left = new Expression.GreaterEqual(left, right);
                }
                case LESS -> {
                    this.consume();
                    Expression right = this.term();
                    left = new Expression.Less(left, right);
                }
                case LESS_EQUAL -> {
                    this.consume();
                    Expression right = this.term();
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
        Expression left = this.comparison();

        while (!this.parseAtEnd()) {
            TokenKind operator = this.currentToken.type;

            switch (operator) {
                case EQUAL -> {
                    this.consume();
                    Expression right = this.comparison();
                    left = new Expression.Equal(left, right);
                }
                case NOT_EQUAL -> {
                    this.consume();
                    Expression right = this.comparison();
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
        Expression left = this.equality();

        while (!this.parseAtEnd()) {
            TokenKind operator = this.currentToken.type;

            switch (operator) {
                case AND -> {
                    this.consume();
                    Expression right = this.equality();
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
        Expression left = this.and();

        while (!this.parseAtEnd()) {
            TokenKind operator = this.currentToken.type;

            switch (operator) {
                case OR -> {
                    this.consume();
                    Expression right = this.and();
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
        Expression or = this.or();

        if (or instanceof Expression.Identifier && this.currentToken.type == TokenKind.ASSIGN) {
            this.consume();
            Expression expression = this.expression();

            return new Expression.Assign(or, expression);
        }

        return or;
    }

    private Expression comma() {
        Expression left = this.assign();

        while (!this.parseAtEnd() && currentToken.type == TokenKind.COMMA) {
            this.consume();
            Expression right = this.assign();
            left = new Expression.Assign(left, right);
        }

        return left;
    }

    private Expression expression() {
        return comma();
    }

    private Statement expressionStatement() {
        int line = this.currentToken.getLine();

        Expression expression = this.expression();

        return new Statement.Expr(expression).setLine(line);
    }

    private Statement printStatement() {
        int line = this.currentToken.getLine();

        this.consume(TokenKind.PRINT, "expected print keyword");

        this.consume(TokenKind.LEFT_PAREN, "expected (");

        Expression expression = this.expression();

        this.consume(TokenKind.RIGHT_PAREN, "expected )");

        return new Statement.Print(expression).setLine(line);
    }

    private Statement blockStatement() {
        int line = this.currentToken.getLine();

        this.consume(TokenKind.LEFT_BRACE, "expected {");

        List<Statement> statements = new ArrayList<>();

        while (!this.parseAtEnd() && this.currentToken.type != TokenKind.RIGHT_BRACE) {
            Statement statement = this.statement();
            statements.add(statement);
        }

        this.consume(TokenKind.RIGHT_BRACE, "expected }");

        return new Statement.Block(statements).setLine(line);
    }

    private Statement ifStatement() {
        int line = this.currentToken.getLine();

        this.consume(TokenKind.IF, "expected if keyword");
        this.consume(TokenKind.LEFT_PAREN, "expected (");

        Expression condition = this.expression();

        this.consume(TokenKind.RIGHT_PAREN, "expected )");

        Statement ifBranch = this.blockStatement();

        if (this.currentToken.type != TokenKind.ELSE) {
            return new Statement.If(condition, ifBranch, null).setLine(line);
        }

        this.consume(TokenKind.ELSE, "expected else keyword");

        Statement elseBranch = switch (this.currentToken.type) {
            case IF -> this.ifStatement();
            default -> this.blockStatement();
        };

        return new Statement.If(condition, ifBranch, elseBranch).setLine(line);
    }

    private Statement whileLoopStatement() {
        int line = this.currentToken.getLine();

        this.consume(TokenKind.WHILE, "expected while keyword");
        this.consume(TokenKind.LEFT_PAREN, "expected (");

        Expression condition = this.expression();

        this.consume(TokenKind.RIGHT_PAREN, "expected )");

        Statement block = this.blockStatement();

        return new Statement.WhileLoop(condition, block).setLine(line);
    }

    private Statement forLoopStatement() {
        int line = this.currentToken.getLine();

        this.consume(TokenKind.FOR, "expected for keyword");

        this.consume(TokenKind.LEFT_PAREN, "expected (");

        Expression variable = this.expression();

        this.consume(TokenKind.SEMICOLON, "expected semicolon after variable declaration");

        Expression condition = this.expression();

        this.consume(TokenKind.SEMICOLON, "expected semicolon after condition");

        Statement increment = this.expressionStatement();

        this.consume(TokenKind.RIGHT_PAREN, "expected )");

        Statement block = this.blockStatement();

        return new Statement.ForLoop(variable, condition, increment, block).setLine(line);
    }

    private Statement statement() {
        Statement statement = switch (this.currentToken.type) {
            case PRINT -> this.printStatement();
            case LEFT_BRACE -> this.blockStatement();
            case IF -> this.ifStatement();
            case WHILE -> this.whileLoopStatement();
            case FOR -> this.forLoopStatement();
            default -> this.expressionStatement();
        };

        if (statement instanceof Statement.Block) {
            return statement;
        }

        this.consume(TokenKind.SEMICOLON, "expected ; at the end of statement");

        return statement;
    }

    private void reset() {
        this.currentIndex = 0;
        this.currentToken = this.tokens.get(0);
        this.line = 1;
    }

    private List<Statement> start() {
        List<Statement> statements = new ArrayList<>();

        while (!this.parseAtEnd()) {
            Statement statement = this.statement();
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
