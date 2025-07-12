package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.TypeAST;
import com.kaori.parser.StatementAST;

public class TypeChecker extends Visitor<TypeAST> {
    public TypeChecker(List<StatementAST> statements) {
        super(statements);
    }

    @Override
    protected void declare(ExpressionAST.Identifier node) {
        String identifier = node.value;

        this.callStack.declare(identifier);
    }

    @Override
    protected TypeAST define(ExpressionAST.Identifier node, TypeAST value) {
        String identifier = node.value;

        return this.callStack.define(identifier, value);
    }

    @Override
    protected TypeAST get(ExpressionAST.Identifier node) {
        String identifier = node.value;

        return this.callStack.get(identifier);
    }

    @Override
    public TypeAST visitBinaryOperator(ExpressionAST.BinaryOperator node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);
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
        TypeAST left = node.left.acceptVisitor(this);
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
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(right)) {
            return right;
        }

        throw KaoriError.TypeError(String.format("invalid = operation between %s and %s", left, right), this.line);
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
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.callStack.enterScope();
        this.visitStatements(statement.statements);
        this.callStack.leaveScope();
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        TypeAST left = statement.type;
        TypeAST right = statement.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.VOID)) {
            throw KaoriError.TypeError(String.format("invalid %s type in variable declaration", left, right),
                    this.line);
        }

        if (left.equals(right)) {
            this.declare(statement.left);
            this.define(statement.left, right);
        } else {
            throw KaoriError.TypeError(String.format("invalid = operation between %s and %s", left, right),
                    this.line);
        }
    }

    @Override
    public void visitExpressionStatement(StatementAST.Expr statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitIfStatement(StatementAST.If statement) {
        TypeAST condition = statement.condition.acceptVisitor(this);

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError(String.format("invalid type for condition: %s", condition), this.line);
        }

        statement.thenBranch.acceptVisitor(this);
        statement.elseBranch.acceptVisitor(this);
    }

    @Override
    public void visitWhileLoopStatement(StatementAST.WhileLoop statement) {
        TypeAST condition = statement.condition.acceptVisitor(this);

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError(String.format("invalid type for condition: %s", condition), this.line);
        }

        statement.block.acceptVisitor(this);
    }

    @Override
    public void visitForLoopStatement(StatementAST.ForLoop statement) {
        statement.variable.acceptVisitor(this);

        TypeAST condition = statement.condition.acceptVisitor(this);

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError(String.format("invalid type for condition: %s", condition), this.line);
        }

        statement.block.acceptVisitor(this);
        statement.increment.acceptVisitor(this);
    }

    @Override
    public void visitFunctionStatement(StatementAST.Function statement) {
        List<TypeAST> parameters = statement.parameters.stream().map(parameter -> parameter.type).toList();
        TypeAST returnType = statement.returnType;
        TypeAST functionType = new TypeAST.Function(parameters, returnType);

        this.declare(statement.name);
        this.define(statement.name, functionType);
    }
}
