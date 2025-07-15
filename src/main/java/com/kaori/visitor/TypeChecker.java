package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.TypeAST;
import com.kaori.visitor.memory.Environment;
import com.kaori.parser.StatementAST;

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
    public TypeAST visitBinaryOperator(ExpressionAST.BinaryOperator node) {
        TypeAST left = this.visitExpression(node.left);
        TypeAST right = this.visitExpression(node.right);
        ExpressionAST.Operator operator = node.operator;

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
    public TypeAST visitUnaryOperator(ExpressionAST.UnaryOperator node) {
        TypeAST left = this.visitExpression(node.left);
        ExpressionAST.Operator operator = node.operator;

        return switch (operator) {
            case MINUS -> {
                if (left.equals(TypeAST.Primitive.BOOLEAN)) {
                    yield TypeAST.Primitive.BOOLEAN;
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
    public TypeAST visitAssign(ExpressionAST.Assign node) {
        ExpressionAST.Identifier identifier = node.left;

        TypeAST right = this.visitExpression(node.right);

        this.define(identifier, right);

        return right;
    }

    @Override
    public TypeAST visitLiteral(ExpressionAST.Literal node) {
        return node.type;
    }

    @Override
    public TypeAST visitIdentifier(ExpressionAST.Identifier node) {
        return this.get(node);
    }

    @Override
    public TypeAST visitFunctionCall(ExpressionAST.FunctionCall node) {
        return this.visitExpression(node.callee);
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        this.visitExpression(statement.expression);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.environment.enterScope();
        this.visitStatements(statement.statements);
        this.environment.exitScope();
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        ExpressionAST.Identifier identifier = statement.left;

        TypeAST left = statement.type;
        TypeAST right = this.visitExpression(statement.right);

        this.declare(identifier, left);
        this.define(identifier, right);
    }

    @Override
    public void visitExpressionStatement(StatementAST.Expr statement) {
        this.visitExpression(statement.expression);
    }

    @Override
    public void visitIfStatement(StatementAST.If statement) {
        TypeAST condition = this.visitExpression(statement.condition);

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError(String.format("invalid type for condition: %s", condition), this.line);
        }

        this.visitStatement(statement.thenBranch);
        this.visitStatement(statement.elseBranch);
    }

    @Override
    public void visitWhileLoopStatement(StatementAST.WhileLoop statement) {
        TypeAST condition = this.visitExpression(statement.condition);

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError(String.format("invalid type for condition: %s", condition), this.line);
        }

        this.visitStatement(statement.block);
    }

    @Override
    public void visitForLoopStatement(StatementAST.ForLoop statement) {
        this.visitStatement(statement.variable);

        TypeAST condition = this.visitExpression(statement.condition);

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError(String.format("invalid type for condition: %s", condition), this.line);
        }

        this.visitStatement(statement.block);
        this.visitExpression(statement.increment);
    }

    @Override
    public void visitFunctionStatement(StatementAST.Function statement) {
        ExpressionAST.Identifier identifier = statement.name;

        List<TypeAST> parameters = statement.parameters.stream().map(parameter -> parameter.type).toList();
        TypeAST returnType = statement.returnType;
        TypeAST functionType = new TypeAST.Function(parameters, returnType);

        this.declare(identifier, functionType);
        this.define(identifier, functionType);

        this.environment.enterScope();

        for (StatementAST.Variable parameter : statement.parameters) {
            this.visitStatement(parameter);
        }

        List<StatementAST> statements = statement.block.statements;
        this.visitStatements(statements);

        this.environment.exitScope();
    }

    @Override
    public void visitFunctionDeclStatement(StatementAST.FunctionDecl statement) {
        ExpressionAST.Identifier identifier = statement.name;

        List<TypeAST> parameters = statement.parameters.stream().map(parameter -> parameter.type).toList();
        TypeAST returnType = statement.returnType;
        TypeAST functionType = new TypeAST.Function(parameters, returnType);

        this.declare(identifier, functionType);
    }

}
