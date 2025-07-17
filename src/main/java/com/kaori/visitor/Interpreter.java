package com.kaori.visitor;

import java.util.List;

import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.error.KaoriError;
import com.kaori.memory.Environment;
import com.kaori.memory.FunctionObject;

public class Interpreter extends Visitor<Object> {
    private final Environment<Object> environment;

    public Interpreter(List<StatementAST> statements) {
        super(statements);
        this.environment = new Environment<>();
    }

    @Override
    protected void declare(ExpressionAST.Identifier identifier, Object value) {
        this.environment.put(identifier, value);
    }

    @Override
    protected void define(ExpressionAST.Identifier identifier, Object value) {
        int distance = this.environment.distance(identifier);

        this.environment.put(identifier, value, distance);
    }

    @Override
    protected Object get(ExpressionAST.Identifier identifier) {
        int distance = this.environment.distance(identifier);
        Object value = this.environment.get(identifier, distance);

        if (value == null) {
            throw KaoriError.RuntimeError(identifier.name() + " is not defined", this.line);
        }

        return value;
    }

    @Override
    public Object visitBinaryOperator(ExpressionAST.BinaryOperator node) {
        Object left = this.visit(node.left());
        Object right = this.visit(node.right());
        ExpressionAST.Operator operator = node.operator();

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
            default -> null;
        };
    }

    @Override
    public Object visitUnaryOperator(ExpressionAST.UnaryOperator node) {
        Object left = this.visit(node.left());
        ExpressionAST.Operator operator = node.operator();

        return switch (operator) {
            case MINUS -> -(Double) left;
            case NOT -> !(Boolean) left;
            default -> null;
        };
    }

    @Override
    public Object visitAssign(ExpressionAST.Assign node) {
        Object value = this.visit(node.right());

        this.define(node.left(), value);

        return value;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal node) {
        return node.value();
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier node) {
        return this.get(node);
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall node) {
        FunctionObject func = (FunctionObject) this.visit(node.callee());

        if (func == null) {
            throw KaoriError.RuntimeError(func + " is not defined", this.line);
        }

        for (StatementAST.Variable parameter : func.parameters()) {
            this.visit(parameter);
        }

        int smallest = Math.min(node.arguments().size(), func.parameters().size());

        for (int i = 0; i < smallest; i++) {
            ExpressionAST.Identifier left = func.parameters().get(i).left();
            Object right = this.visit(node.arguments().get(i));

            this.define(left, right);
        }

        this.visitStatements(func.statements());

        return null;
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        Object expression = this.visit(statement.expression());

        System.out.println(expression);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.visitStatements(statement.statements());
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        Object right = this.visit(statement.right());

        this.declare(statement.left(), null);
        this.define(statement.left(), right);
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

    @Override
    public void visitFunctionStatement(StatementAST.Function statement) {
        this.declare(statement.name(), null);

        FunctionObject func = new FunctionObject(statement.parameters(), statement.block().statements());

        this.define(statement.name(), func);

    }

    @Override
    public void visitFunctionDeclStatement(StatementAST.FunctionDecl statement) {
        this.declare(statement.name(), null);
    }
}
