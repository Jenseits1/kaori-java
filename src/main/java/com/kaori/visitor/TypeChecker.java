package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.ExpressionAST.BinaryOperator;
import com.kaori.parser.ExpressionAST.FunctionCall;
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

        return switch (node.operator) {

            /* ───── Arithmetic ─────────────────────────────────────────── */
            case PLUS, MINUS, MULTIPLY, DIVIDE, MODULO, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL -> {
                if (left.equals(TypeAST.Primitive.NUMBER) &&
                        right.equals(TypeAST.Primitive.NUMBER)) {
                    yield TypeAST.Primitive.NUMBER;
                }
                throw KaoriError.TypeError("Arithmetic operators require two numbers", this.line);
            }

            /* ───── Logical ────────────────────────────────────────────── */
            case AND, OR -> {
                if (left.equals(TypeAST.Primitive.BOOLEAN) &&
                        right.equals(TypeAST.Primitive.BOOLEAN)) {
                    yield TypeAST.Primitive.BOOLEAN;
                }
                throw KaoriError.TypeError("Arithmetic operators require two numbers", this.line);
            }

            /* ───── Equality / Inequality ─────────────────────────────── */
            case EQUAL, NOT_EQUAL -> {
                if (left.equals(right)) {
                    yield TypeAST.Primitive.BOOLEAN;
                }
                throw KaoriError.TypeError("Arithmetic operators require two numbers", this.line);
            }

            /* ───── Assignment ─────────────────────────────────────────── */
            case ASSIGN -> {
                if (left.equals(right)) {
                    yield left; // or `right`, they are equal at this point
                }

                throw KaoriError.TypeError("Arithmetic operators require two numbers", this.line);
            }

            /* ───── (Unary operators are checked elsewhere) ────────────── */
            default -> throw KaoriError.TypeError("Arithmetic operators require two numbers", this.line);
        };
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
    public TypeAST visitNot(ExpressionAST.Not node) {
        TypeAST left = node.left.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.BOOLEAN)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError(String.format("invalid ! operation for", left), this.line);
    }

    @Override
    public TypeAST visitNegation(ExpressionAST.Negation node) {
        TypeAST left = node.left.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.NUMBER;
        }

        throw KaoriError.TypeError(String.format("invalid - operation for", left), this.line);
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

        if (left.equals(right)) {
            this.declare(statement.left);
            this.define(statement.left, right);
        } else {
            KaoriError.TypeError(String.format("invalid = operation between %s and %s", left, right),
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
        // TODO Auto-generated method stub
    }

    @Override
    public TypeAST visitFunctionCall(FunctionCall node) {
        throw new UnsupportedOperationException("Unimplemented method 'visitFunctionCall'");
    }
}
