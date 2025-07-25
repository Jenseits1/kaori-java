package com.kaori.runtime;

import java.util.List;
import java.util.Stack;

import com.kaori.compiler.bytecode.Bytecode;
import com.kaori.compiler.bytecode.Instruction;
import com.kaori.compiler.bytecode.Opcode;

public class KaoriVM {
    private final List<Instruction> instructions;
    private final Stack<Object> stack;
    private final CallStack callStack;
    private int index;

    public KaoriVM(Bytecode bytecode) {
        this.instructions = bytecode.instructions();
        this.stack = new Stack<>();
        this.callStack = new CallStack();
        this.index = 0;
    }

    public void advance() {
        this.index++;
    }

    public void jumpTo(Object index) {
        this.index = (Integer) index;
    }

    public void run() {
        while (this.index < instructions.size()) {
            Instruction instruction = instructions.get(index);

            switch (instruction.opcode()) {
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
                        LESS_EQUAL -> {
                    this.evalBinary(instruction.opcode());
                    this.advance();
                }

                case NOT,
                        NEGATE -> {
                    this.evalUnary(instruction.opcode());
                    this.advance();
                }

                case LOAD_LOCAL -> {
                    int offset = (Integer) instruction.operand();
                    Object value = this.callStack.loadLocal(offset);

                    this.stack.add(value);
                    this.advance();
                }
                case LOAD_GLOBAL -> {
                    int offset = (Integer) instruction.operand();
                    Object value = this.callStack.loadGlobal(offset);

                    this.stack.add(value);
                    this.advance();
                }
                case STORE_LOCAL -> {
                    int offset = (Integer) instruction.operand();
                    Object top = this.stack.pop();

                    this.callStack.storeLocal(top, offset);
                    this.advance();
                }
                case STORE_GLOBAL -> {
                    int offset = (Integer) instruction.operand();
                    Object top = this.stack.pop();

                    this.callStack.storeGlobal(top, offset);
                    this.advance();
                }

                case DECLARE -> {
                    Object top = this.stack.pop();
                    this.callStack.declare(top);

                    this.advance();
                }
                case PUSH_CONST -> {
                    this.stack.push(instruction.operand());

                    this.advance();
                }

                case JUMP_IF_FALSE -> {
                    Object top = this.stack.pop();

                    if ((Boolean) top == false) {
                        this.jumpTo(instruction.operand());
                    } else {
                        this.advance();
                    }

                }
                case JUMP -> {
                    this.jumpTo(instruction.operand());
                }

                case ENTER_SCOPE -> {
                    this.callStack.enterScope();
                    this.advance();
                }
                case EXIT_SCOPE -> {
                    this.callStack.exitScope();
                    this.advance();
                }
                case PRINT -> {
                    Object top = this.stack.pop();
                    System.out.println(top);

                    this.advance();
                }
            }

        }

    }

    public void evalBinary(Opcode opcode) {
        Object right = this.stack.pop();
        Object left = this.stack.pop();

        switch (opcode) {
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

    public void evalUnary(Opcode opcode) {
        Object left = this.stack.pop();

        switch (opcode) {
            case NOT -> this.stack.push(!(Boolean) left);
            case NEGATE -> this.stack.push(-(Double) left);
        }
    }
}
