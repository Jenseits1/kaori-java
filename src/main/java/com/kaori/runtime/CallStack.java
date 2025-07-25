package com.kaori.runtime;

import java.util.Stack;

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

    public void define(T value, int offset, boolean local) {
        if (local) {
            offset = framePointers.peek() + offset;
        }

        this.stack.set(offset, value);
    }

    public T get(int offset, boolean local) {
        if (local) {
            offset = framePointers.peek() + offset;
        }

        return this.stack.get(offset);
    }

    public void enterFunction() {
        this.framePointers.push(this.index);
    }

    public void exitFunction() {
        this.index = this.framePointers.pop();
    }
}
