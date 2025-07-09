package com.kaori.visitor.memory;

import java.util.Stack;

public class CallStack<T> {
    private final Stack<Environment<T>> frames;

    public CallStack() {

    }

    Environment<T> global() {
        return this.frames.firstElement();
    }

    void pushFrame() {
        Environment<T> environment = new Environment<>();
        this.frames.add(environment);

    }

    void popFrame() {
        this.frames.pop();
    }
}
