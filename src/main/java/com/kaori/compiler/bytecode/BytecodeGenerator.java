package com.kaori.compiler.bytecode;

import java.util.ArrayList;
import java.util.List;

import com.kaori.compiler.Visitor;
import com.kaori.compiler.syntax.DeclarationAST;
import com.kaori.compiler.syntax.ExpressionAST;
import com.kaori.compiler.syntax.StatementAST;

public class BytecodeGenerator extends Visitor<Object> {
    public final List<Instruction> instructions;

    public BytecodeGenerator(List<DeclarationAST> declarations) {
        super(declarations);
        this.instructions = new ArrayList<>();
    }

    public Bytecode bytecode() {
        this.visitDeclarations(this.declarations);

        return new Bytecode(instructions);
    }

    public void emit(Opcode opcode) {
        Instruction instruction = new Instruction(opcode, null);

        this.instructions.add(instruction);
    }

    public void emit(Opcode opcode, Object operand) {
        Instruction instruction = new Instruction(opcode, operand);

        this.instructions.add(instruction);
    }

    @Override
    public Object visitBinaryExpression(ExpressionAST.BinaryExpression expression) {
        this.visit(expression.left());
        this.visit(expression.right());

        ExpressionAST.BinaryOperator operator = expression.operator();

        Opcode opcode = switch (operator) {
            case PLUS -> Opcode.PLUS;
            case MINUS -> Opcode.MINUS;
            case MULTIPLY -> Opcode.MULTIPLY;
            case DIVIDE -> Opcode.DIVIDE;
            case MODULO -> Opcode.MODULO;
            case GREATER -> Opcode.GREATER;
            case GREATER_EQUAL -> Opcode.GREATER_EQUAL;
            case LESS -> Opcode.LESS;
            case LESS_EQUAL -> Opcode.LESS_EQUAL;
            case AND -> Opcode.AND;
            case OR -> Opcode.OR;
            case EQUAL -> Opcode.EQUAL;
            case NOT_EQUAL -> Opcode.NOT_EQUAL;
        };

        this.emit(opcode);

        return null;
    }

    @Override
    public Object visitUnaryExpression(ExpressionAST.UnaryExpression expression) {
        this.visit(expression.left());

        ExpressionAST.UnaryOperator operator = expression.operator();

        Opcode opcode = switch (operator) {
            case NEGATE -> Opcode.NEGATE;
            case NOT -> Opcode.NOT;
        };

        this.emit(opcode);

        return null;
    }

    @Override
    public Object visitAssign(ExpressionAST.Assign expression) {
        this.visit(expression.right());

        ExpressionAST.Identifier identifier = expression.left();

        if (identifier.local()) {
            this.emit(Opcode.STORE_LOCAL, identifier.offset());
        } else {
            this.emit(Opcode.STORE_GLOBAL, identifier.offset());
        }

        return null;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal expression) {
        this.emit(Opcode.PUSH_CONST, expression.value());

        return null;
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier expression) {

        if (expression.local()) {
            this.emit(Opcode.LOAD_LOCAL, expression.offset());
        } else {
            this.emit(Opcode.LOAD_GLOBAL, expression.offset());
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

        this.emit(Opcode.PRINT);
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

        this.emit(Opcode.LOAD_LOCAL);
    }

    @Override
    public void visitFunctionDeclaration(DeclarationAST.Function declaration) {

    }

    @Override
    public void visitFunctionDefinition(DeclarationAST.Function declaration) {

    }
}
