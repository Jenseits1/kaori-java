package com.kaori.vm;

import java.util.Stack;

public class CallStack {
    public final Object[] stack;
    private final Stack<Integer> scopes;
    private final Stack<Integer> framePointers;
    private int index;

    public CallStack() {
        final int stackMaxSize = 1_000;

        this.stack = new Object[stackMaxSize];
        this.scopes = new Stack<>();
        this.framePointers = new Stack<>();
        this.index = 0;

        this.framePointers.push(0);
    }

    private void updateIndex() {
        this.index++;
    }

    public void declare(Object value) {
        this.stack[index] = value;

        this.updateIndex();
    }

    public void define(Object value, int offset, boolean local) {
        if (local) {
            offset = framePointers.peek() + offset;
        }

        this.stack[offset] = value;
    }

    public Object get(int offset, boolean local) {
        if (local) {
            offset = framePointers.peek() + offset;
        }

        return this.stack[offset];
    }

    public Object loadLocal(int offset) {
        offset = framePointers.peek() + offset;

        return this.stack[offset];
    }

    public Object loadGlobal(int offset) {
        return this.stack[offset];
    }

    public void storeLocal(Object value, int offset) {
        offset = framePointers.peek() + offset;

        this.stack[offset] = value;
    }

    public void storeGlobal(Object value, int offset) {
        this.stack[offset] = value;
    }

    public void enterFunction() {
        this.framePointers.push(this.index);
    }

    public void exitFunction() {
        this.index = this.framePointers.pop();
    }

    public void enterScope() {
        this.scopes.add(this.index);
    }

    public void exitScope() {
        this.index = this.scopes.pop();
    }
}
