package com.kaori.visitor;

import java.util.List;

import com.kaori.ast.DeclarationAST;
import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.error.KaoriError;
import com.kaori.memory.Environment;
import com.kaori.memory.FunctionObject;

public class Interpreter extends Visitor<Object> {
    private final Environment<Object> environment;

    public Interpreter(List<DeclarationAST> declarations) {
        super(declarations);
        this.environment = new Environment<>();
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
        int distance = identifier.distance();

        this.environment.define(identifier.name(), value, distance);

        return value;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal expression) {
        return expression.value();
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier expression) {
        int distance = expression.distance();
        Object value = this.environment.get(distance);

        if (value == null) {
            throw KaoriError.RuntimeError(expression.name() + " is not defined", this.line);
        }

        return value;
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall expression) {
        FunctionObject functionObject = (FunctionObject) this.visit(expression.callee());

        this.environment.enterScope();

        for (StatementAST.Variable parameter : functionObject.parameters()) {
            this.visit(parameter);
        }

        int smallest = Math.min(expression.arguments().size(), functionObject.parameters().size());

        for (int i = 0; i < smallest; i++) {

            ExpressionAST.Identifier left = functionObject.parameters().get(i).left();
            Object right = this.visit(expression.arguments().get(i));

            int distance = left.distance();

            this.environment.define(left.name(), right, distance);
        }

        this.visitDeclarations(functionObject.declarations());

        this.environment.exitScope();

        return null;
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        Object expression = this.visit(statement.expression());

        System.out.println(expression);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.environment.enterScope();
        this.visitDeclarations(statement.declarations());
        this.environment.exitScope();
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
        int distance = identifier.distance();

        this.environment.define(identifier.name(), right, distance);
    }

    @Override
    public void visitFunctionDeclaration(DeclarationAST.Function declaration) {
        ExpressionAST.Identifier identifier = declaration.name();
        int distance = identifier.distance();

        if (declaration.block() == null) {
            this.environment.define(identifier.name(), null, distance);
            return;
        }

        FunctionObject functionObject = new FunctionObject(declaration.parameters(),
                declaration.block().declarations());
        this.environment.define(identifier.name(), functionObject, distance);
    }
}