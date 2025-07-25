package com.kaori.vm;

import java.util.Stack;

import com.kaori.compiler.resolver.DeclarationRef;

public class CallStack<T> {
    public final Stack<T> stack;
    private final Stack<Integer> framePointers;
    private int index;

    public CallStack() {
        this.stack = new Stack<>();
        this.framePointers = new Stack<>();
        this.index = 0;

        final int stackMaxSize = 1_000;

        stack.setSize(stackMaxSize);

        this.framePointers.push(0);
    }

    public void updateIndex() {
        this.index++;

    }

    public void declare(T value) {
        this.stack.set(index, value);

        this.updateIndex();
    }

    public void define(T value, DeclarationRef reference) {
        int index = reference.offset();

        if (reference.local()) {
            index += framePointers.peek();
        }

        this.stack.set(index, value);
    }

    public T get(DeclarationRef reference) {
        int index = reference.offset();

        if (reference.local()) {
            index += framePointers.peek();
        }

        return this.stack.get(index);
    }

    public void enterFunction() {
        this.framePointers.push(this.index);
    }

    public void exitFunction() {
        this.index = this.framePointers.pop();
    }
}
