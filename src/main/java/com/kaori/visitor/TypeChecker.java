package com.kaori.visitor;

import java.util.List;

import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.ast.TypeAST;
import com.kaori.error.KaoriError;
import com.kaori.memory.Environment;

public class TypeChecker extends Visitor<TypeAST> {
    private final Environment<TypeAST> environment;

    public TypeChecker(List<StatementAST> statements) {
        super(statements);
        this.environment = new Environment<>();
    }

    @Override
    protected void declare(ExpressionAST.Identifier identifier, TypeAST value) {
        if (value.equals(TypeAST.Primitive.VOID)) {
            throw KaoriError.TypeError(String.format("invalid %s type for declaration", value),
                    this.line);
        }

        this.environment.put(identifier, value);
    }

    @Override
    protected void define(ExpressionAST.Identifier identifier, TypeAST value) {
        int distance = this.environment.distance(identifier);

        TypeAST previousType = this.environment.get(identifier, distance);

        if (!previousType.equals(value)) {
            throw KaoriError.TypeError(
                    String.format("invalid definition with type %s for type %s", value, previousType),
                    this.line);
        }

        this.environment.put(identifier, value, distance);
    }

    @Override
    protected TypeAST get(ExpressionAST.Identifier identifier) {
        int distance = this.environment.distance(identifier);

        return this.environment.get(identifier, distance);
    }

    @Override
    public TypeAST visitBinaryOperator(ExpressionAST.BinaryOperator expression) {
        TypeAST left = this.visit(expression.left());
        TypeAST right = this.visit(expression.right());
        ExpressionAST.Operator operator = expression.operator();

        return switch (operator) {
            case PLUS, MINUS, MULTIPLY, DIVIDE, MODULO -> {
                if (left.equals(TypeAST.Primitive.NUMBER) &&
                        right.equals(TypeAST.Primitive.NUMBER)) {
                    yield TypeAST.Primitive.NUMBER;
                }

                throw KaoriError.TypeError(
                        String.format("invalid %s operation between %s and %s", operator, left, right),
                        this.line);
            }
            case GREATER, GREATER_EQUAL, LESS, LESS_EQUAL -> {
                if (left.equals(TypeAST.Primitive.NUMBER) &&
                        right.equals(TypeAST.Primitive.NUMBER)) {
                    yield TypeAST.Primitive.BOOLEAN;
                }

                throw KaoriError.TypeError(
                        String.format("invalid %s operation between %s and %s", operator, left, right),
                        this.line);
            }
            case AND, OR -> {
                if (left.equals(TypeAST.Primitive.BOOLEAN) &&
                        right.equals(TypeAST.Primitive.BOOLEAN)) {
                    yield TypeAST.Primitive.BOOLEAN;
                }

                throw KaoriError.TypeError(
                        String.format("invalid %s operation between %s and %s", operator, left, right),
                        this.line);
            }
            case EQUAL, NOT_EQUAL -> {
                if (left.equals(right)) {
                    yield TypeAST.Primitive.BOOLEAN;
                }

                throw KaoriError.TypeError(
                        String.format("invalid %s operation between %s and %s", operator, left, right),
                        this.line);
            }
            default -> null;
        };
    }

    @Override
    public TypeAST visitUnaryOperator(ExpressionAST.UnaryOperator expression) {
        TypeAST left = this.visit(expression.left());
        ExpressionAST.Operator operator = expression.operator();

        return switch (operator) {
            case MINUS -> {
                if (left.equals(TypeAST.Primitive.NUMBER)) {
                    yield TypeAST.Primitive.NUMBER;
                }

                throw KaoriError.TypeError(String.format("invalid %s operation for %s", operator, left),
                        this.line);
            }
            case NOT -> {
                if (left.equals(TypeAST.Primitive.BOOLEAN)) {
                    yield TypeAST.Primitive.BOOLEAN;
                }

                throw KaoriError.TypeError(String.format("invalid %s operation for %s", operator, left),
                        this.line);
            }
            default -> null;
        };
    }

    @Override
    public TypeAST visitAssign(ExpressionAST.Assign expression) {
        ExpressionAST.Identifier identifier = expression.left();

        TypeAST right = this.visit(expression.right());

        this.define(identifier, right);

        return right;
    }

    @Override
    public TypeAST visitLiteral(ExpressionAST.Literal expression) {
        return expression.type();
    }

    @Override
    public TypeAST visitIdentifier(ExpressionAST.Identifier expression) {
        return this.get(expression);
    }

    @Override
    public TypeAST visitFunctionCall(ExpressionAST.FunctionCall expression) {
        TypeAST type = this.visit(expression.callee());

        if (!(type instanceof TypeAST.Function func)) {
            throw KaoriError.TypeError(String.format("invalid %s type is not a function", type),
                    this.line);
        }
        int smallest = Math.min(func.parameters().size(), expression.arguments().size());

        for (int i = 0; i < smallest; i++) {
            TypeAST argument = this.visit(expression.arguments().get(i));
            TypeAST parameter = func.parameters().get(i);

            if (!argument.equals(parameter)) {
                throw KaoriError.TypeError(
                        String.format("invalid argument of type %s for parameter of type %s", argument, parameter),
                        this.line);
            }
        }

        return func.returnType();
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        this.visit(statement.expression());
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.environment.enterScope();
        this.visitStatements(statement.statements());
        this.environment.exitScope();
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        ExpressionAST.Identifier identifier = statement.left();

        TypeAST left = statement.type();
        TypeAST right = this.visit(statement.right());

        this.declare(identifier, left);
        this.define(identifier, right);
    }

    @Override
    public void visitExpressionStatement(StatementAST.Expr statement) {
        this.visit(statement.expression());
    }

    @Override
    public void visitIfStatement(StatementAST.If statement) {
        TypeAST condition = this.visit(statement.condition());

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError(String.format("invalid type for condition: %s", condition), this.line);
        }

        this.visit(statement.thenBranch());
        this.visit(statement.elseBranch());
    }

    @Override
    public void visitWhileLoopStatement(StatementAST.WhileLoop statement) {
        TypeAST condition = this.visit(statement.condition());

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError(String.format("invalid type for condition: %s", condition), this.line);
        }

        this.visit(statement.block());
    }

    @Override
    public void visitForLoopStatement(StatementAST.ForLoop statement) {
        this.visit(statement.variable());

        TypeAST condition = this.visit(statement.condition());

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError(String.format("invalid type for condition: %s", condition), this.line);
        }

        this.visit(statement.block());
        this.visit(statement.increment());
    }

    @Override
    public void visitFunctionStatement(StatementAST.Function statement) {
        ExpressionAST.Identifier identifier = statement.name();

        int distance = this.environment.distance(identifier);

        if (distance != 0) {
            this.declare(identifier, statement.type());
        }

        this.define(identifier, statement.type());

        this.environment.enterScope();

        for (StatementAST.Variable parameter : statement.parameters()) {
            this.visit(parameter);
        }

        List<StatementAST> statements = statement.block().statements();
        this.visitStatements(statements);

        this.environment.exitScope();
    }

    @Override
    public void visitFunctionDeclStatement(StatementAST.FunctionDecl statement) {
        ExpressionAST.Identifier identifier = statement.name();

        this.declare(identifier, statement.type());
    }

}
