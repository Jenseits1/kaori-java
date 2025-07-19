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
    public TypeAST visitBinaryExpression(ExpressionAST.BinaryExpression expression) {
        TypeAST left = this.visit(expression.left());
        TypeAST right = this.visit(expression.right());
        ExpressionAST.BinaryOperator operator = expression.operator();

        TypeAST type = switch (operator) {
            case PLUS, MINUS, MULTIPLY, DIVIDE, MODULO ->
                left.equals(TypeAST.Primitive.NUMBER) &&
                        right.equals(TypeAST.Primitive.NUMBER)
                                ? TypeAST.Primitive.NUMBER
                                : TypeAST.Primitive.VOID;

            case GREATER, GREATER_EQUAL, LESS, LESS_EQUAL ->
                left.equals(TypeAST.Primitive.NUMBER) &&
                        right.equals(TypeAST.Primitive.NUMBER)
                                ? TypeAST.Primitive.BOOLEAN
                                : TypeAST.Primitive.VOID;

            case AND, OR ->
                left.equals(TypeAST.Primitive.BOOLEAN) &&
                        right.equals(TypeAST.Primitive.BOOLEAN)
                                ? TypeAST.Primitive.BOOLEAN
                                : TypeAST.Primitive.VOID;

            case EQUAL, NOT_EQUAL ->
                left.equals(right)
                        ? TypeAST.Primitive.BOOLEAN
                        : TypeAST.Primitive.VOID;
        };

        if (type.equals(TypeAST.Primitive.VOID)) {
            throw KaoriError.TypeError(
                    String.format("invalid %s operation between %s and %s", operator, left, right),
                    this.line);
        }

        return type;
    }

    @Override
    public TypeAST visitUnaryExpression(ExpressionAST.UnaryExpression expression) {
        TypeAST left = this.visit(expression.left());
        ExpressionAST.UnaryOperator operator = expression.operator();

        TypeAST type = switch (operator) {
            case MINUS -> left.equals(TypeAST.Primitive.NUMBER) ? TypeAST.Primitive.NUMBER : TypeAST.Primitive.VOID;
            case NOT -> left.equals(TypeAST.Primitive.BOOLEAN) ? TypeAST.Primitive.BOOLEAN : TypeAST.Primitive.VOID;

        };

        if (type.equals(TypeAST.Primitive.VOID)) {
            throw KaoriError.TypeError(String.format("invalid %s operation for %s", operator, left),
                    this.line);
        }

        return type;
    }

    @Override
    public TypeAST visitAssign(ExpressionAST.Assign expression) {
        ExpressionAST.Identifier identifier = expression.left();

        TypeAST left = this.visit(identifier);
        TypeAST right = this.visit(expression.right());

        if (!left.equals(right)) {
            throw KaoriError.TypeError(
                    String.format("invalid variable declaration with type %s for type %s", left, right),
                    this.line);
        }

        this.environment.define(identifier.name(), right, identifier.reference());

        return right;
    }

    @Override
    public TypeAST visitLiteral(ExpressionAST.Literal expression) {
        return expression.type();
    }

    @Override
    public TypeAST visitIdentifier(ExpressionAST.Identifier expression) {
        return this.environment.get(expression.reference());
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

        if (!left.equals(right)) {
            throw KaoriError.TypeError(
                    String.format("invalid variable declaration with type %s for type %s", left, right),
                    this.line);
        }

        this.environment.declare(identifier.name(), right);
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

        int reference = identifier.reference();

        TypeAST previousType = reference < 0 ? statement.type() : this.environment.get(reference);

        if (!statement.type().equals(previousType)) {
            throw KaoriError.TypeError(
                    String.format("invalid function declaration with type %s for type %s", statement.type(),
                            previousType),
                    this.line);
        }

        this.environment.define(identifier.name(), statement.type(), reference);

        this.environment.enterScope();

        for (StatementAST.Variable parameter : statement.parameters()) {
            this.visit(parameter);
        }

        List<StatementAST> statements = statement.block().statements();
        this.visitStatements(statements);

        this.environment.exitScope();
    }

}
