package com.kaori.memory;

import java.util.Stack;

import com.kaori.memory.resolver.DeclarationRef;

public class CallStack<T> {
    public final Stack<T> stack;
    public final Stack<Integer> framePointers;

    public CallStack() {
        this.stack = new Stack<>();
        this.framePointers = new Stack<>();

        final int stackMaxSize = 10_000;

        // stack.setSize(stackMaxSize);

        this.framePointers.push(0);
    }

    public void define(T value, DeclarationRef reference) {
        int index = reference.offset();

        if (reference.local()) {
            index += framePointers.peek();
        }

        if (index >= this.stack.size()) {
            this.stack.push(value);
        } else {
            this.stack.set(index, value);
        }

    }

    public T get(DeclarationRef reference) {
        int index = reference.offset();

        if (reference.local()) {
            index += framePointers.peek();
        }

        return this.stack.get(index);
    }

    public void enterFunction() {
        int index = this.stack.size();

        this.framePointers.push(index);
    }

    public void exitFunction() {
        this.framePointers.pop();
    }
}
