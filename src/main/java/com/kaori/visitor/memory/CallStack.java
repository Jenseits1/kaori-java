package com.kaori.visitor.memory;

import java.util.HashMap;
import java.util.Stack;

public class CallStack<T> {
    private final Stack<FunctionFrame<T>> frames;

    public CallStack() {
        this.frames = new Stack<>();

        this.push();
    }

    private FunctionFrame<T> mainFrame() {
        return this.frames.firstElement();
    }

    private FunctionFrame<T> currentFrame() {
        return this.frames.peek();
    }

    public void push() {
        FunctionFrame<T> frame = new FunctionFrame<>();

        this.frames.add(frame);
    }

    public void pop() {
        this.frames.pop();
    }

    public boolean declared(String identifier) {
        HashMap<String, T> currentScope = this.currentFrame().scopes.peek();

        return currentScope.containsKey(identifier);
    }

    public void declare(String identifier) {
        HashMap<String, T> currentScope = this.currentFrame().scopes.peek();

        currentScope.put(identifier, null);
    }

    public T define(String identifier, T value) {
        FunctionFrame<T> currentFrame = this.currentFrame();

        int index = currentFrame.scopes.size();

        while (index >= 0) {
            HashMap<String, T> currentScope = currentFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                currentScope.put(identifier, value);
                return value;
            }

            index++;
        }

        FunctionFrame<T> mainFrame = this.mainFrame();

        index = mainFrame.scopes.size();

        while (index >= 0) {
            HashMap<String, T> currentScope = mainFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                currentScope.put(identifier, value);
                return value;
            }

            index++;
        }

        return null;
    }

    public T get(String identifier) {
        FunctionFrame<T> currentFrame = this.currentFrame();

        int index = currentFrame.scopes.size();

        while (index >= 0) {
            HashMap<String, T> currentScope = currentFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return currentScope.get(identifier);
            }

            index++;
        }

        FunctionFrame<T> mainFrame = this.mainFrame();

        index = mainFrame.scopes.size();

        while (index >= 0) {
            HashMap<String, T> currentScope = mainFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return currentScope.get(identifier);
            }

            index++;
        }

        return null;
    }

    public boolean find(String identifier) {
        FunctionFrame<T> currentFrame = this.currentFrame();

        int index = currentFrame.scopes.size();

        while (index >= 0) {
            HashMap<String, T> currentScope = currentFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return true;
            }

            index++;
        }

        FunctionFrame<T> mainFrame = this.mainFrame();

        index = mainFrame.scopes.size();

        while (index >= 0) {
            HashMap<String, T> currentScope = mainFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return true;
            }

            index++;
        }

        return false;
    }
}
