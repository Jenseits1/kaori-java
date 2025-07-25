package com.kaori.compiler.bytecode;

import java.util.ArrayList;
import java.util.List;

import com.kaori.compiler.Visitor;
import com.kaori.compiler.bytecode.Instruction.InstructionKind;
import com.kaori.compiler.syntax.DeclarationAST;
import com.kaori.compiler.syntax.ExpressionAST;
import com.kaori.compiler.syntax.StatementAST;

public class BytecodeGenerator extends Visitor<Object> {
    public final List<Instruction> bytecode;

    public BytecodeGenerator(List<DeclarationAST> declarations) {
        super(declarations);
        this.bytecode = new ArrayList<>();
    }

    public List<Instruction> generateBytecode() {
        this.visitDeclarations(this.declarations);

        return this.bytecode;
    }

    public void emit(InstructionKind kind) {
        Instruction instruction = new Instruction(kind, null);

        this.bytecode.add(instruction);
    }

    public void emit(InstructionKind kind, Object operand) {
        Instruction instruction = new Instruction(kind, operand);

        this.bytecode.add(instruction);
    }

    @Override
    public Object visitBinaryExpression(ExpressionAST.BinaryExpression expression) {
        this.visit(expression.left());
        this.visit(expression.right());

        ExpressionAST.BinaryOperator operator = expression.operator();

        InstructionKind kind = switch (operator) {
            case PLUS -> InstructionKind.PLUS;
            case MINUS -> InstructionKind.MINUS;
            case MULTIPLY -> InstructionKind.MULTIPLY;
            case DIVIDE -> InstructionKind.DIVIDE;
            case MODULO -> InstructionKind.MODULO;
            case GREATER -> InstructionKind.GREATER;
            case GREATER_EQUAL -> InstructionKind.GREATER_EQUAL;
            case LESS -> InstructionKind.LESS;
            case LESS_EQUAL -> InstructionKind.LESS_EQUAL;
            case AND -> InstructionKind.AND;
            case OR -> InstructionKind.OR;
            case EQUAL -> InstructionKind.EQUAL;
            case NOT_EQUAL -> InstructionKind.NOT_EQUAL;
        };

        this.emit(kind);

        return null;
    }

    @Override
    public Object visitUnaryExpression(ExpressionAST.UnaryExpression expression) {
        this.visit(expression.left());

        ExpressionAST.UnaryOperator operator = expression.operator();

        InstructionKind kind = switch (operator) {
            case MINUS -> InstructionKind.MINUS;
            case NOT -> InstructionKind.NOT;
        };

        this.emit(kind);

        return null;
    }

    @Override
    public Object visitAssign(ExpressionAST.Assign expression) {
        this.visit(expression.right());

        ExpressionAST.Identifier identifier = expression.left();

        if (identifier.local()) {
            this.emit(InstructionKind.STORE_LOCAL, identifier.offset());
        } else {
            this.emit(InstructionKind.STORE_GLOBAL, identifier.offset());
        }

        return null;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal expression) {
        this.emit(InstructionKind.PUSH_CONST, expression.value());

        return null;
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier expression) {

        if (expression.local()) {
            this.emit(InstructionKind.LOAD_LOCAL, expression.offset());
        } else {
            this.emit(InstructionKind.LOAD_GLOBAL, expression.offset());
        }

        return null;
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall expression) {

        return null;
    }

    @Override
    public void visitPrintStatement(StatementAST.Print statement) {
        this.visit(statement.expression());

        this.emit(InstructionKind.PRINT);
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

    /* Declarations */
    @Override
    public void visitVariableDeclaration(DeclarationAST.Variable declaration) {
        this.visit(declaration.right());

        this.emit(InstructionKind.LOAD_LOCAL);
    }

    @Override
    public void visitFunctionDeclaration(DeclarationAST.Function declaration) {

    }

    @Override
    public void visitFunctionDefinition(DeclarationAST.Function declaration) {

    }
}
