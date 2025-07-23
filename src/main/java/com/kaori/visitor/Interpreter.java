package com.kaori.visitor;

import java.util.ArrayList;
import java.util.List;

import com.kaori.ast.DeclarationAST;
import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.error.KaoriError;
import com.kaori.memory.CallStack;
import com.kaori.memory.FunctionObject;
import com.kaori.memory.resolver.DeclarationRef;

public class Interpreter extends Visitor<Object> {
    public final CallStack<Object> callStack;

    public Interpreter(List<DeclarationAST> declarations) {
        super(declarations);
        this.callStack = new CallStack<>();
    }

    @Override
    public Object visitBinaryExpression(ExpressionAST.BinaryExpression expression) {
        Object left = this.visit(expression.left());
        Object right = this.visit(expression.right());
        ExpressionAST.BinaryOperator operator = expression.operator();

        return switch (operator) {
            case PLUS -> (Double) left + (Double) right;
            case MINUS -> (Double) left - (Double) right;
            case MULTIPLY -> (Double) left * (Double) right;
            case DIVIDE -> {
                if ((Double) right == 0) {
                    throw KaoriError.RuntimeError(
                            String.format("invalid %s operation between %s and %s", operator, left, right),
                            this.line);
                }

                yield (Double) left / (Double) right;
            }

            case MODULO -> {
                if ((Double) right == 0) {
                    throw KaoriError.RuntimeError(
                            String.format("invalid %s operation between %s and %s", operator, left, right),
                            this.line);
                }

                yield (Double) left % (Double) right;
            }
            case GREATER -> (Double) left > (Double) right;
            case GREATER_EQUAL -> (Double) left >= (Double) right;
            case LESS -> (Double) left < (Double) right;
            case LESS_EQUAL -> (Double) left <= (Double) right;
            case AND -> (Boolean) left && (Boolean) right;
            case OR -> (Boolean) left || (Boolean) right;
            case EQUAL -> left.equals(right);
            case NOT_EQUAL -> !left.equals(right);
        };
    }

    @Override
    public Object visitUnaryExpression(ExpressionAST.UnaryExpression expression) {
        Object left = this.visit(expression.left());
        ExpressionAST.UnaryOperator operator = expression.operator();

        return switch (operator) {
            case MINUS -> -(Double) left;
            case NOT -> !(Boolean) left;
        };
    }

    @Override
    public Object visitAssign(ExpressionAST.Assign expression) {
        Object value = this.visit(expression.right());
        ExpressionAST.Identifier identifier = expression.left();
        DeclarationRef reference = identifier.reference();

        this.callStack.define(value, reference);

        return value;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal expression) {
        return expression.value();
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier expression) {
        DeclarationRef reference = expression.reference();
        Object value = this.callStack.get(reference);

        if (value == null) {
            throw KaoriError.RuntimeError(expression.name() + " is not defined", this.line);
        }

        return value;
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall expression) {
        FunctionObject functionObject = (FunctionObject) this.visit(expression.callee());

        List<Object> arguments = new ArrayList<>();

        for (ExpressionAST argument : expression.arguments()) {
            arguments.add(this.visit(argument));
        }

        this.callStack.enterFunction();

        for (StatementAST.Variable parameter : functionObject.parameters()) {
            this.visit(parameter);
        }

        int smallest = Math.min(arguments.size(), functionObject.parameters().size());

        for (int i = 0; i < smallest; i++) {
            DeclarationRef reference = functionObject.parameters().get(i).left().reference();
            Object argument = arguments.get(i);
            this.callStack.define(argument, reference);
        }

        this.visitDeclarations(functionObject.declarations());

        this.callStack.exitFunction();

        return null;

        // "foo", "n",
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        Object expression = this.visit(statement.expression());

        System.out.println(expression);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.visitDeclarations(statement.declarations());
    }

    @Override
    public void visitExpressionStatement(StatementAST.Expr statement) {
        this.visit(statement.expression());
    }

    @Override
    public void visitIfStatement(StatementAST.If statement) {
        Object condition = this.visit(statement.condition());

        if ((Boolean) condition == true) {
            this.visit(statement.thenBranch());
        } else {
            this.visit(statement.elseBranch());
        }
    }

    @Override
    public void visitWhileLoopStatement(StatementAST.WhileLoop statement) {
        while (true) {
            Object condition = this.visit(statement.condition());

            if ((Boolean) condition == false)
                break;

            this.visit(statement.block());
        }
    }

    @Override
    public void visitForLoopStatement(StatementAST.ForLoop statement) {
        this.visit(statement.variable());

        while (true) {
            Object condition = this.visit(statement.condition());

            if ((Boolean) condition == false)
                break;

            this.visit(statement.block());
            this.visit(statement.increment());
        }
    }

    /* Declarations */
    @Override
    public void visitVariableDeclaration(DeclarationAST.Variable declaration) {
        Object right = this.visit(declaration.right());
        ExpressionAST.Identifier identifier = declaration.left();
        DeclarationRef reference = identifier.reference();

        this.callStack.define(right, reference);
    }

    @Override
    public void visitFunctionDeclaration(DeclarationAST.Function declaration) {
        ExpressionAST.Identifier identifier = declaration.name();
        DeclarationRef reference = identifier.reference();

        FunctionObject functionObject = new FunctionObject(declaration.parameters(),
                declaration.block().declarations());

        this.callStack.define(functionObject, reference);
    }

    @Override
    public void visitFunctionDefinition(DeclarationAST.Function declaration) {

    }
}