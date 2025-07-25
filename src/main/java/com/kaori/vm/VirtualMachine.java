package com.kaori.vm;

import java.util.List;
import java.util.Stack;

import com.kaori.vm.Instruction.InstructionKind;

public class VirtualMachine {
    public final List<Instruction> bytecode;
    public final Stack<Object> stack;

    public VirtualMachine(List<Instruction> bytecode) {
        this.bytecode = bytecode;
        this.stack = new Stack<>();
    }

    public void run() {
        int index = 0;

        while (index < bytecode.size()) {
            Instruction instruction = bytecode.get(index);

            switch (instruction.kind) {
                case PLUS,
                        MINUS,
                        MULTIPLY,
                        DIVIDE,
                        MODULO,
                        AND,
                        OR,
                        NOT_EQUAL,
                        EQUAL,
                        GREATER,
                        GREATER_EQUAL,
                        LESS,
                        LESS_EQUAL ->
                    this.evalBinary(instruction.kind);
                case NOT,
                        NEGATE ->
                    this.evalUnary(instruction.kind);

                case LOAD_LOCAL -> throw new UnsupportedOperationException("Instruction not implemented: LOAD_LOCAL");
                case LOAD_GLOBAL -> throw new UnsupportedOperationException("Instruction not implemented: LOAD_GLOBAL");
                case STORE_LOCAL -> throw new UnsupportedOperationException("Instruction not implemented: STORE_LOCAL");
                case STORE_GLOBAL ->
                    throw new UnsupportedOperationException("Instruction not implemented: STORE_GLOBAL");

                case PUSH_CONST -> {
                    Object value = instruction.operand;

                    this.stack.push(value);
                }

                case JUMP_IF_FALSE ->
                    throw new UnsupportedOperationException("Instruction not implemented: JUMP_IF_FALSE");
                case JUMP_IF_TRUE ->
                    throw new UnsupportedOperationException("Instruction not implemented: JUMP_IF_TRUE");

                case PRINT -> {
                    Object value = this.stack.pop();
                    System.out.println(value);
                }
            }

            index++;

        }

    }

    public void evalBinary(InstructionKind kind) {
        Object right = this.stack.pop();
        Object left = this.stack.pop();

        switch (kind) {
            case PLUS -> this.stack.push((Double) left + (Double) right);
            case MINUS -> this.stack.push((Double) left - (Double) right);
            case MULTIPLY -> this.stack.push((Double) left * (Double) right);
            case DIVIDE -> this.stack.push((Double) left / (Double) right);
            case MODULO -> this.stack.push((Double) left % (Double) right);

            case AND -> this.stack.push((Boolean) left && (Boolean) right);
            case OR -> this.stack.push((Boolean) left || (Boolean) right);

            case NOT_EQUAL -> this.stack.push(!left.equals(right));
            case EQUAL -> this.stack.push(left.equals(right));
            case GREATER -> this.stack.push((Double) left > (Double) right);
            case GREATER_EQUAL -> this.stack.push((Double) left >= (Double) right);
            case LESS -> this.stack.push((Double) left < (Double) right);
            case LESS_EQUAL -> this.stack.push((Double) left <= (Double) right);
        }
    }

    public void evalUnary(InstructionKind kind) {
        Object left = this.stack.pop();

        switch (kind) {
            case NOT -> this.stack.push(!(Boolean) left);
            case NEGATE -> this.stack.push(-(Double) left);
        }
    }
}
