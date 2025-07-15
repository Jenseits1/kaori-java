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
    private ExpressionAST functionCall(ExpressionAST callee) {
        if (this.tokens.getCurrent() != TokenKind.LEFT_PAREN) {
            return callee;
        }

        this.tokens.consume(TokenKind.LEFT_PAREN);

        List<ExpressionAST> arguments = new ArrayList<>();

        while (!this.tokens.atEnd() && this.tokens.getCurrent() != TokenKind.RIGHT_PAREN) {
            ExpressionAST argument = this.expression();
            arguments.add(argument);

            if (this.tokens.getCurrent() == TokenKind.RIGHT_PAREN) {
                break;
            }

            this.tokens.consume(TokenKind.COMMA);
        }

        this.tokens.consume(TokenKind.RIGHT_PAREN);

        ExpressionAST call = new ExpressionAST.FunctionCall(callee, arguments);

        return this.functionCall(call);
    }

    private ExpressionAST postfixUnary(ExpressionAST.Identifier identifier) {
        return switch (this.tokens.getCurrent()) {
            case INCREMENT -> {
                ExpressionAST literal = new ExpressionAST.Literal(TypeAST.Primitive.NUMBER, 1.0);
                ExpressionAST add = new ExpressionAST.BinaryOperator(identifier, literal, ExpressionAST.Operator.PLUS);
                this.tokens.consume();
                yield new ExpressionAST.Assign(identifier, add);
            }
            case DECREMENT -> {
                ExpressionAST literal = new ExpressionAST.Literal(TypeAST.Primitive.NUMBER, 1.0);
                ExpressionAST subtract = new ExpressionAST.BinaryOperator(identifier, literal,
                        ExpressionAST.Operator.MINUS);
                this.tokens.consume();
                yield new ExpressionAST.Assign(identifier, subtract);
            }
            case LEFT_PAREN -> this.functionCall(identifier);
            default -> identifier;
        };
    }

    private ExpressionAST parenthesis() {
        this.tokens.consume(TokenKind.LEFT_PAREN);

        ExpressionAST expression = this.expression();

        this.tokens.consume(TokenKind.RIGHT_PAREN);

        return expression;
    }

    private ExpressionAST.Identifier identifier() {
        String lexeme = this.tokens.getLexeme();
        this.tokens.consume(TokenKind.IDENTIFIER);

        return new ExpressionAST.Identifier(lexeme);
    }

    private ExpressionAST primary() {
        if (this.tokens.atEnd()) {
            throw KaoriError.SyntaxError("expected a valid operand", this.tokens.getLine());
        }

        return switch (this.tokens.getCurrent()) {
            case BOOLEAN_LITERAL -> {
                String lexeme = this.tokens.getLexeme();
                boolean value = Boolean.parseBoolean(lexeme);
                ExpressionAST literal = new ExpressionAST.Literal(TypeAST.Primitive.BOOLEAN, value);
                this.tokens.consume();

                yield literal;
            }
            case STRING_LITERAL -> {
                String lexeme = this.tokens.getLexeme();
                String value = lexeme.substring(1, lexeme.length() - 1);
                TypeAST type = TypeAST.Primitive.STRING;
                ExpressionAST literal = new ExpressionAST.Literal(type, value);
                this.tokens.consume();

                yield literal;
            }
            case NUMBER_LITERAL -> {
                String lexeme = this.tokens.getLexeme();
                Double value = Double.parseDouble(lexeme);
                TypeAST type = TypeAST.Primitive.NUMBER;
                ExpressionAST literal = new ExpressionAST.Literal(type, value);
                this.tokens.consume();

                yield literal;
            }
            case IDENTIFIER -> {
                ExpressionAST.Identifier identifier = this.identifier();
                yield this.postfixUnary(identifier);

            }

            case LEFT_PAREN -> this.parenthesis();
            default -> throw KaoriError.SyntaxError(
                    String.format("expected valid operand instead of %s", this.tokens.getCurrent()),
                    this.tokens.getLine());
        };
    }

    private ExpressionAST prefixUnary() {
        return switch (this.tokens.getCurrent()) {
            case MINUS -> {
                this.tokens.consume();
                yield new ExpressionAST.UnaryOperator(this.prefixUnary(), ExpressionAST.Operator.MINUS);
            }
            case NOT -> {
                this.tokens.consume();
                yield new ExpressionAST.UnaryOperator(this.prefixUnary(), ExpressionAST.Operator.NOT);
            }
            case PLUS -> {
                this.tokens.consume();
                yield this.prefixUnary();

            }
            default -> this.primary();
        };
    }

    private ExpressionAST factor() {
        ExpressionAST left = this.prefixUnary();

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case MULTIPLY -> {
                    this.tokens.consume();
                    ExpressionAST right = this.prefixUnary();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.MULTIPLY);
                }
                case DIVIDE -> {
                    this.tokens.consume();
                    ExpressionAST right = this.prefixUnary();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.DIVIDE);
                }
                case MODULO -> {
                    this.tokens.consume();
                    ExpressionAST right = this.prefixUnary();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.MODULO);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    private ExpressionAST term() {
        ExpressionAST left = this.factor();

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case PLUS -> {
                    this.tokens.consume();
                    ExpressionAST right = this.factor();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.PLUS);
                }
                case MINUS -> {
                    this.tokens.consume();
                    ExpressionAST right = this.factor();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.MINUS);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    private ExpressionAST comparison() {
        ExpressionAST left = this.term();

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case GREATER -> {
                    this.tokens.consume();
                    ExpressionAST right = this.term();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.GREATER);
                }
                case GREATER_EQUAL -> {
                    this.tokens.consume();
                    ExpressionAST right = this.term();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.GREATER_EQUAL);
                }
                case LESS -> {
                    this.tokens.consume();
                    ExpressionAST right = this.term();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.LESS);
                }
                case LESS_EQUAL -> {
                    this.tokens.consume();
                    ExpressionAST right = this.term();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.LESS_EQUAL);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    private ExpressionAST equality() {
        ExpressionAST left = this.comparison();

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case EQUAL -> {
                    this.tokens.consume();
                    ExpressionAST right = this.comparison();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.EQUAL);
                }
                case NOT_EQUAL -> {
                    this.tokens.consume();
                    ExpressionAST right = this.comparison();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.NOT_EQUAL);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    private ExpressionAST and() {
        ExpressionAST left = this.equality();

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case AND -> {
                    this.tokens.consume();
                    ExpressionAST right = this.equality();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.AND);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    private ExpressionAST or() {
        ExpressionAST left = this.and();

        while (!this.tokens.atEnd()) {
            TokenKind operator = this.tokens.getCurrent();

            switch (operator) {
                case OR -> {
                    this.tokens.consume();
                    ExpressionAST right = this.and();
                    left = new ExpressionAST.BinaryOperator(left, right, ExpressionAST.Operator.OR);
                }
                default -> {
                    return left;
                }
            }
        }

        return left;
    }

    private ExpressionAST assign() {
        ExpressionAST.Identifier left = this.identifier();
        this.tokens.consume(TokenKind.ASSIGN);
        ExpressionAST right = this.expression();

        return new ExpressionAST.Assign(left, right);
    }

    private ExpressionAST expression() {
        if (this.tokens.lookAhead(TokenKind.IDENTIFIER, TokenKind.ASSIGN)) {
            return this.assign();
        }

        return this.or();
    }

    /* Types */
    private TypeAST type() {
        TypeAST type = switch (this.tokens.getCurrent()) {
            case IDENTIFIER -> this.primitiveType();
            default -> throw KaoriError.SyntaxError("expected valid type", this.tokens.getLine());
        };

        return type;
    }

    private TypeAST primitiveType() {
        String lexeme = this.tokens.getLexeme();
        this.tokens.consume(TokenKind.IDENTIFIER);

        TypeAST type = switch (lexeme) {
            case "str" -> TypeAST.Primitive.STRING;
            case "bool" -> TypeAST.Primitive.BOOLEAN;
            case "f64" -> TypeAST.Primitive.NUMBER;
            case "void" -> TypeAST.Primitive.VOID;
            default -> throw KaoriError.SyntaxError("expected primitive types", this.tokens.getLine());
        };

        return type;
    }

    /* Statements */
    private StatementAST.Expr expressionStatement() {
        int line = this.tokens.getLine();

        ExpressionAST expression = this.expression();

        return new StatementAST.Expr(line, expression);
    }

    private StatementAST.Variable variableStatement() {
        int line = this.tokens.getLine();

        ExpressionAST.Identifier left = this.identifier();
        this.tokens.consume(TokenKind.COLON);

        TypeAST type = this.type();

        if (this.tokens.getCurrent() != TokenKind.ASSIGN) {
            ExpressionAST right = new ExpressionAST.Literal(type, null);
            return new StatementAST.Variable(line, left, right, type);
        }

        this.tokens.consume(TokenKind.ASSIGN);

        ExpressionAST right = this.expression();

        return new StatementAST.Variable(line, left, right, type);
    }

    private StatementAST.Print printStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.PRINT);

        this.tokens.consume(TokenKind.LEFT_PAREN);

        ExpressionAST expression = this.expression();

        this.tokens.consume(TokenKind.RIGHT_PAREN);

        return new StatementAST.Print(line, expression);
    }

    private StatementAST.Block blockStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.LEFT_BRACE);

        List<StatementAST> statements = new ArrayList<>();

        while (!this.tokens.atEnd() && this.tokens.getCurrent() != TokenKind.RIGHT_BRACE) {
            StatementAST statement = this.statement();
            statements.add(statement);
        }

        this.tokens.consume(TokenKind.RIGHT_BRACE);

        return new StatementAST.Block(line, statements);
    }

    private StatementAST.If ifStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.IF);

        ExpressionAST condition = this.expression();

        StatementAST.Block thenBranch = this.blockStatement();

        if (this.tokens.getCurrent() != TokenKind.ELSE) {

            StatementAST.Block elseBranch = new StatementAST.Block(line);
            return new StatementAST.If(line, condition, thenBranch, elseBranch);
        }

        this.tokens.consume(TokenKind.ELSE);

        StatementAST elseBranch = switch (this.tokens.getCurrent()) {
            case IF -> this.ifStatement();
            default -> this.blockStatement();
        };

        return new StatementAST.If(line, condition, thenBranch, elseBranch);
    }

    private StatementAST.WhileLoop whileLoopStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.WHILE);

        ExpressionAST condition = this.expression();

        StatementAST.Block block = this.blockStatement();

        return new StatementAST.WhileLoop(line, condition, block);
    }

    private StatementAST.ForLoop forLoopStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.FOR);

        StatementAST.Variable variable = this.variableStatement();

        this.tokens.consume(TokenKind.SEMICOLON);

        ExpressionAST condition = this.expression();

        this.tokens.consume(TokenKind.SEMICOLON);

        ExpressionAST increment = this.expression();

        StatementAST.Block block = this.blockStatement();

        return new StatementAST.ForLoop(line, variable, condition, increment, block);
    }

    private StatementAST functionStatement() {
        int line = this.tokens.getLine();

        this.tokens.consume(TokenKind.FUNCTION);

        ExpressionAST.Identifier name = this.identifier();

        this.tokens.consume(TokenKind.LEFT_PAREN);

        List<StatementAST.Variable> parameters = new ArrayList<>();

        while (!this.tokens.atEnd() && this.tokens.getCurrent() != TokenKind.RIGHT_PAREN) {
            StatementAST.Variable parameter = this.variableStatement();
            parameters.add(parameter);

            if (this.tokens.getCurrent() == TokenKind.RIGHT_PAREN) {
                break;
            }

            this.tokens.consume(TokenKind.COMMA);
        }

        this.tokens.consume(TokenKind.RIGHT_PAREN);
        this.tokens.consume(TokenKind.COLON);

        TypeAST returnType = this.type();

        TypeAST.Function type = new TypeAST.Function(parameters.stream().map(parameter -> parameter.type()).toList(),
                returnType);

        if (this.tokens.getCurrent() != TokenKind.LEFT_BRACE) {
            return new StatementAST.FunctionDecl(line, name, parameters, type);
        }

        StatementAST.Block block = this.blockStatement();

        return new StatementAST.Function(line, name, parameters, type, block);
    }

    private StatementAST statement() {
        StatementAST statement = switch (this.tokens.getCurrent()) {
            case PRINT -> this.printStatement();
            case LEFT_BRACE -> this.blockStatement();
            case IF -> this.ifStatement();
            case WHILE -> this.whileLoopStatement();
            case FOR -> this.forLoopStatement();
            case FUNCTION -> this.functionStatement();
            default -> {
                if (this.tokens.lookAhead(TokenKind.IDENTIFIER, TokenKind.COLON)) {
                    yield this.variableStatement();
                }

                yield this.expressionStatement();
            }
        };

        if (statement instanceof StatementAST.Expr || statement instanceof StatementAST.Variable
                || statement instanceof StatementAST.Print || statement instanceof StatementAST.FunctionDecl) {
            this.tokens.consume(TokenKind.SEMICOLON);
        }

        return statement;
    }

    private List<StatementAST> start() {
        List<StatementAST> statements = new ArrayList<>();

        while (!this.tokens.atEnd()) {
            StatementAST statement = this.statement();
            statements.add(statement);

        }

        return statements;
    }

    public List<StatementAST> parse() {
        List<StatementAST> statements = start();

        return statements;
    }
}
