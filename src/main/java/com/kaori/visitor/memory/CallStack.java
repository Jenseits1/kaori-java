package com.kaori.visitor.memory;

import java.util.Stack;

public class CallStack<T> {
    private final Stack<StackFrame<T>> frames;

    public CallStack() {
        this.frames = new Stack<>();

        this.push();
    }

    public StackFrame<T> mainFrame() {
        return this.frames.firstElement();
    }

    public StackFrame<T> currentFrame() {
        return this.frames.peek();
    }

    public void push() {
        StackFrame<T> frame = new StackFrame<>();

        this.frames.add(frame);
    }

    public void pop() {
        this.frames.pop();
    }
}
