package com.kaori.visitor;

import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.parser.ExpressionAST;
import com.kaori.parser.ExpressionAST.FunctionCall;
import com.kaori.visitor.memory.Environment;
import com.kaori.parser.TypeAST;
import com.kaori.parser.StatementAST;

public class TypeChecker extends Visitor<TypeAST> {
    public TypeChecker(List<StatementAST> statements) {
        super(statements);
    }

    @Override
    public TypeAST visitAdd(ExpressionAST.Add node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER) && right.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.NUMBER;
        }

        if (left.equals(TypeAST.Primitive.STRING) && right.equals(TypeAST.Primitive.STRING)) {
            return TypeAST.Primitive.STRING;
        }

        throw KaoriError.TypeError("expected number or string operands for '+'", this.line);
    }

    @Override
    public TypeAST visitSubtract(ExpressionAST.Subtract node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER) && right.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '-'", this.line);
    }

    @Override
    public TypeAST visitMultiply(ExpressionAST.Multiply node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER) && right.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '*'", this.line);
    }

    @Override
    public TypeAST visitDivide(ExpressionAST.Divide node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER) && right.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '/'", this.line);
    }

    @Override
    public TypeAST visitModulo(ExpressionAST.Modulo node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER) && right.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected number operands for '%'", this.line);
    }

    @Override
    public TypeAST visitAnd(ExpressionAST.And node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.BOOLEAN) && right.equals(TypeAST.Primitive.BOOLEAN)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operands for '&&'", this.line);
    }

    @Override
    public TypeAST visitOr(ExpressionAST.Or node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.BOOLEAN) && right.equals(TypeAST.Primitive.BOOLEAN)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operands for '||'", this.line);
    }

    @Override
    public TypeAST visitEqual(ExpressionAST.Equal node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(right)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected operands of same type for '=='", this.line);
    }

    @Override
    public TypeAST visitNotEqual(ExpressionAST.NotEqual node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (!left.equals(right)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected operands of same type for '!='", this.line);
    }

    @Override
    public TypeAST visitGreater(ExpressionAST.Greater node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER) && right.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>'", this.line);
    }

    @Override
    public TypeAST visitGreaterEqual(ExpressionAST.GreaterEqual node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER) && right.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '>='", this.line);
    }

    @Override
    public TypeAST visitLess(ExpressionAST.Less node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER) && right.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<'", this.line);
    }

    @Override
    public TypeAST visitLessEqual(ExpressionAST.LessEqual node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER) && right.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected number operands for '<='", this.line);
    }

    @Override
    public TypeAST visitAssign(ExpressionAST.Assign node) {
        TypeAST left = node.left.acceptVisitor(this);
        TypeAST right = node.right.acceptVisitor(this);

        if (left.equals(right)) {
            return right;
        }

        throw KaoriError.TypeError("expected the same type on variable assignment", this.line);
    }

    @Override
    public TypeAST visitLiteral(ExpressionAST.Literal node) {
        return node.type;
    }

    @Override
    public TypeAST visitIdentifier(ExpressionAST.Identifier node) {
        Environment<TypeAST> env = this.environment.find(node);
        return env.get(node);
    }

    @Override
    public TypeAST visitNot(ExpressionAST.Not node) {
        TypeAST value = node.left.acceptVisitor(this);

        if (value.equals(TypeAST.Primitive.BOOLEAN)) {
            return TypeAST.Primitive.BOOLEAN;
        }

        throw KaoriError.TypeError("expected boolean operand for '!'", this.line);
    }

    @Override
    public TypeAST visitNegation(ExpressionAST.Negation node) {
        TypeAST left = node.left.acceptVisitor(this);

        if (left.equals(TypeAST.Primitive.NUMBER)) {
            return TypeAST.Primitive.NUMBER;
        }

        throw KaoriError.TypeError("expected float operand for unary '-'", this.line);
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        statement.expression.acceptVisitor(this);
    }

    @Override
    public void visitBlockStatement(StatementAST.Block statement) {
        this.environment = new Environment<>(environment);
        this.visitStatements(statement.statements);
        this.environment = environment.getPrevious();
    }

    @Override
    public void visitVariableStatement(StatementAST.Variable statement) {
        TypeAST left = statement.type;
        TypeAST right = statement.right.acceptVisitor(this);

        if (left.equals(right)) {
            this.environment.set(statement.left, right);
        } else {
            throw KaoriError.TypeError("expected " + left + " type for " + statement.left.value, this.line);
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
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.thenBranch.acceptVisitor(this);
        statement.elseBranch.acceptVisitor(this);
    }

    @Override
    public void visitWhileLoopStatement(StatementAST.WhileLoop statement) {
        TypeAST condition = statement.condition.acceptVisitor(this);

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
        }

        statement.block.acceptVisitor(this);
    }

    @Override
    public void visitForLoopStatement(StatementAST.ForLoop statement) {
        statement.variable.acceptVisitor(this);

        TypeAST condition = statement.condition.acceptVisitor(this);

        if (!condition.equals(TypeAST.Primitive.BOOLEAN)) {
            throw KaoriError.TypeError("expected boolean value for condition", this.line);
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
