package com.kaori.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.kaori.ast.DeclarationAST;
import com.kaori.ast.ExpressionAST;
import com.kaori.ast.StatementAST;
import com.kaori.memory.FunctionObject;
import com.kaori.memory.resolver.DeclarationRef;
import com.kaori.vm.Instruction;
import com.kaori.vm.Instruction.InstructionKind;

public class BytecodeGenerator extends Visitor<Object> {
    public final Stack<Object> bytecode;

    public BytecodeGenerator(List<DeclarationAST> declarations) {
        super(declarations);
        this.bytecode = new Stack<>();
    }

    public void emit(InstructionKind kind) {
        Instruction instruction = new Instruction(kind, 0);

        this.bytecode.add(instruction);
    }

    public void emit(InstructionKind kind, int operand) {
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
        DeclarationRef reference = identifier.reference();

        return value;
    }

    @Override
    public Object visitLiteral(ExpressionAST.Literal expression) {
        return expression.value();
    }

    @Override
    public Object visitIdentifier(ExpressionAST.Identifier expression) {
        DeclarationRef reference = expression.reference();

        return null;
    }

    @Override
    public Object visitFunctionCall(ExpressionAST.FunctionCall expression) {
        FunctionObject functionObject = (FunctionObject) this.visit(expression.callee());

        List<Object> arguments = new ArrayList<>();

        for (int i = 0; i < functionObject.parameters().size(); i++) {
            if (i < expression.arguments().size()) {
                ExpressionAST argument = expression.arguments().get(i);
                arguments.add(this.visit(argument));
            } else {
                arguments.add(null);
            }

        }

        for (int i = 0; i < functionObject.parameters().size(); i++) {
            Object defaultValue = this.visit(functionObject.parameters().get(i).right());
            Object argument = arguments.get(i);

        }

        this.visitDeclarations(functionObject.declarations());

        return null;
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

    /* Declarations */
    @Override
    public void visitVariableDeclaration(DeclarationAST.Variable declaration) {
        Object right = this.visit(declaration.right());

    }

    @Override
    public void visitFunctionDeclaration(DeclarationAST.Function declaration) {
        FunctionObject functionObject = new FunctionObject(declaration.parameters(),
                declaration.block().declarations());

    }

    @Override
    public void visitFunctionDefinition(DeclarationAST.Function declaration) {

    }
}
