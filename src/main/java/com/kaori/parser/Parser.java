package com.kaori.parser;

import java.util.ArrayList;
import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.token.TokenKind;
import com.kaori.token.TokenStream;

public class Parser {
    private final TokenStream tokens;

    public Parser(TokenStream tokens) {
        this.tokens = tokens;
    }

    /* Expressions */
    private Expression postfixUnary() {
        throw KaoriError.SyntaxError("not found", this.tokens.getLine());
    }

    private Expression parenthesis() {
        this.tokens.consume(TokenKind.LEFT_PAREN);

        Expression expression = expression();

        this.tokens.consume(TokenKind.RIGHT_PAREN);

        return expression;
    }

    private Expression identifier() {
        String lexeme = this.tokens.getLexeme();
        this.tokens.consume(TokenKind.IDENTIFIER);
        Expression identifier = new Expression.Identifier(lexeme);

        return identifier;
    }

    private Expression primary() {
        if (this.tokens.atEnd()) {
            throw KaoriError.SyntaxError("expected valid operand", this.tokens.getLine());
        }

        return switch (this.tokens.getCurrent()) {
            case BOOLEAN_LITERAL -> {
                boolean value = Boolean.parseBoolean(this.tokens.getLexeme());
                Expression literal = new Expression.Literal(KaoriType.Primitive.BOOLEAN, value);
                this.tokens.consume();

                yield literal;
            }
            case STRING_LITERAL -> {
                String value = this.tokens.getLexeme();
                KaoriType type = KaoriType.Primitive.STRING;
                Expression literal = new Expression.Literal(type,
                        value.substring(1, value.length() - 1));
                this.tokens.consume();

                yield literal;
            }
            case NUMBER_LITERAL -> {
                Double value = Double.parseDouble(this.tokens.getLexeme());
                KaoriType type = KaoriType.Primitive.NUMBER;
                Expression literal = new Expression.Literal(type, value);
                this.tokens.consume();

                yield literal;
            }
            case IDENTIFIER -> this.identifier();
            case LEFT_PAREN -> this.parenthesis();
            default -> throw KaoriError.SyntaxError("expected valid operand", this.tokens.getLine());
        };

    }

    private Expression prefixUnary() {
        return switch (this.tokens.getCurrent()) {
            case MINUS -> {
                this.tokens.consume();
                yield new Expression.Negation(this.prefixUnary());
            }
            case NOT -> {
                this.tokens.consume();
                yield new Expression.Not(this.prefixUnary());
            }
            case PLUS -> {
                this.tokens.consume();

                yield this.prefixUnary();

            }
            default -> this.primary();
        };
    }

    private Expression factor() {
        Expression left = this.prefixUnary();

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case MULTIPLY -> {
                    this.tokens.consume();
                    Expression right = this.prefixUnary();
                    left = new Expression.Multiply(left, right);
                }
                case DIVIDE -> {
                    this.tokens.consume();
                    Expression right = this.prefixUnary();
                    left = new Expression.Divide(left, right);
                }
                case MODULO -> {
                    this.tokens.consume();
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

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case PLUS -> {
                    this.tokens.consume();
                    Expression right = this.factor();
                    left = new Expression.Add(left, right);
                }
                case MINUS -> {
                    this.tokens.consume();
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

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case GREATER -> {
                    this.tokens.consume();
                    Expression right = this.term();
                    left = new Expression.Greater(left, right);
                }
                case GREATER_EQUAL -> {
                    this.tokens.consume();
                    Expression right = this.term();
                    left = new Expression.GreaterEqual(left, right);
                }
                case LESS -> {
                    this.tokens.consume();
                    Expression right = this.term();
                    left = new Expression.Less(left, right);
                }
                case LESS_EQUAL -> {
                    this.tokens.consume();
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

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case EQUAL -> {
                    this.tokens.consume();
                    Expression right = this.comparison();
                    left = new Expression.Equal(left, right);
                }
                case NOT_EQUAL -> {
                    this.tokens.consume();
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

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case AND -> {
                    this.tokens.consume();
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

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case OR -> {
                    this.tokens.consume();
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
        Expression left = this.identifier();
        this.tokens.consume(TokenKind.ASSIGN);
        Expression right = this.expression();

        return new Expression.Assign(left, right);
    }

    private Expression expression() {
        if (this.tokens.lookAhead(TokenKind.IDENTIFIER, TokenKind.ASSIGN)) {
            return assign();
        }

        return this.or();
    }

    /* Types */
    private KaoriType type() {
        KaoriType type = switch (this.tokens.getCurrent()) {
            case IDENTIFIER -> this.primitiveType();
            default -> throw KaoriError.SyntaxError("expected valid type", this.tokens.getLine());
        };

        return type;
    }

    private KaoriType primitiveType() {
        String lexeme = this.tokens.getLexeme();
        this.tokens.consume(TokenKind.IDENTIFIER);

        KaoriType type = switch (lexeme) {
            case "str" -> KaoriType.Primitive.STRING;
            case "bool" -> KaoriType.Primitive.BOOLEAN;
            case "f64" -> KaoriType.Primitive.NUMBER;
            default -> throw KaoriError.SyntaxError("expected primitive types", this.tokens.getLine());
        };

        return type;
    }

    /* Statements */
    private Statement expressionStatement() {
        int line = this.tokens.getLine();

        Expression expression = this.expression();

        return new Statement.Expr(expression).setLine(line);
    }

    private Statement variableStatement() {
        int line = this.tokens.getLine();

        Expression left = this.identifier();
        this.tokens.consume(TokenKind.COLON);

        KaoriType type = this.type();

        if (this.tokens.getCurrent() != TokenKind.ASSIGN) {
            Expression right = new Expression.Literal(type, null);
            return new Statement.Variable(left, right, type).setLine(line);
        }

        this.tokens.consume(TokenKind.ASSIGN);

        Expression right = this.expression();

        return new Statement.Variable(left, right, type).setLine(line);
    }

    private Statement printStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.PRINT);

        this.tokens.consume(TokenKind.LEFT_PAREN);

        Expression expression = this.expression();

        this.tokens.consume(TokenKind.RIGHT_PAREN);

        return new Statement.Print(expression).setLine(line);
    }

    private Statement blockStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.LEFT_BRACE);

        List<Statement> statements = new ArrayList<>();

        while (!this.tokens.atEnd() && this.tokens.getCurrent() != TokenKind.RIGHT_BRACE) {
            Statement statement = this.statement();
            statements.add(statement);
        }

        this.tokens.consume(TokenKind.RIGHT_BRACE);

        return new Statement.Block(statements).setLine(line);
    }

    private Statement ifStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.IF);

        Expression condition = this.expression();

        Statement thenBranch = this.blockStatement();

        if (this.tokens.getCurrent() != TokenKind.ELSE) {
            Statement elseBranch = new Statement.Block();
            return new Statement.If(condition, thenBranch, elseBranch).setLine(line);
        }

        this.tokens.consume(TokenKind.ELSE);

        Statement elseBranch = switch (this.tokens.getCurrent()) {
            case IF -> this.ifStatement();
            default -> this.blockStatement();
        };

        return new Statement.If(condition, thenBranch, elseBranch).setLine(line);
    }

    private Statement whileLoopStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.WHILE);

        Expression condition = this.expression();

        Statement block = this.blockStatement();

        return new Statement.WhileLoop(condition, block).setLine(line);
    }

    private Statement forLoopStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.FOR);

        Statement variable = this.variableStatement();

        this.tokens.consume(TokenKind.SEMICOLON);

        Expression condition = this.expression();

        this.tokens.consume(TokenKind.SEMICOLON);

        Statement increment = this.expressionStatement();

        Statement block = this.blockStatement();

        return new Statement.ForLoop(variable, condition, increment, block).setLine(line);
    }

    private Statement statement() {
        Statement statement = switch (this.tokens.getCurrent()) {
            case PRINT -> this.printStatement();
            case LEFT_BRACE -> this.blockStatement();
            case IF -> this.ifStatement();
            case WHILE -> this.whileLoopStatement();
            case FOR -> this.forLoopStatement();
            default -> {
                if (this.tokens.lookAhead(TokenKind.IDENTIFIER, TokenKind.COLON)) {
                    yield this.variableStatement();
                }

                yield this.expressionStatement();
            }
        };

        if (statement instanceof Statement.Expr || statement instanceof Statement.Variable
                || statement instanceof Statement.Print) {
            this.tokens.consume(TokenKind.SEMICOLON);
        }

        return statement;
    }

    private List<Statement> start() {
        List<Statement> statements = new ArrayList<>();

        while (!this.tokens.atEnd()) {
            Statement statement = this.statement();
            statements.add(statement);

        }

        return statements;
    }

    public List<Statement> parse() {
        List<Statement> statements = start();

        return statements;
    }
}
