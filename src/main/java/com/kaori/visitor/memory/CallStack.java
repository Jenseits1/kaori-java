package com.kaori.visitor.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CallStack<T> {
    private final Stack<Frame<T>> frames;

    public CallStack() {
        this.frames = new Stack<>();
    }

    private Frame<T> mainFrame() {
        return this.frames.firstElement();
    }

    private Frame<T> currentFrame() {
        return this.frames.peek();
    }

    public void enterScope() {
        Map<String, T> scope = new HashMap<>();

        this.currentFrame().scopes.add(scope);
    }

    public void leaveScope() {
        this.currentFrame().scopes.pop();
    }

    public void push() {
        Frame<T> frame = new Frame<>();

        this.frames.add(frame);
    }

    public void pop() {
        this.frames.pop();
    }

    public boolean declared(String identifier) {
        Map<String, T> currentScope = this.currentFrame().scopes.peek();

        return currentScope.containsKey(identifier);
    }

    public void declare(String identifier) {
        Map<String, T> currentScope = this.currentFrame().scopes.peek();

        currentScope.put(identifier, null);
    }

    public T define(String identifier, T value) {
        Frame<T> currentFrame = this.currentFrame();

        for (int index = currentFrame.scopes.size() - 1; index >= 0; index--) {
            Map<String, T> currentScope = currentFrame.scopes.get(index);
            if (currentScope.containsKey(identifier)) {
                currentScope.put(identifier, value);
                return value;
            }
        }

        Frame<T> mainFrame = this.mainFrame();

        for (int index = mainFrame.scopes.size() - 1; index >= 0; index--) {
            Map<String, T> currentScope = mainFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                currentScope.put(identifier, value);
                return value;
            }
        }

        return null;
    }

    public T get(String identifier) {
        Frame<T> currentFrame = this.currentFrame();

        for (int index = currentFrame.scopes.size() - 1; index >= 0; index--) {
            Map<String, T> currentScope = currentFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return currentScope.get(identifier);
            }
        }

        Frame<T> mainFrame = this.mainFrame();

        for (int index = mainFrame.scopes.size() - 1; index >= 0; index--) {
            Map<String, T> currentScope = mainFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return currentScope.get(identifier);
            }
        }

        return null;
    }

    public boolean find(String identifier) {
        Frame<T> currentFrame = this.currentFrame();

        for (int index = currentFrame.scopes.size() - 1; index >= 0; index--) {
            Map<String, T> currentScope = currentFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return true;
            }
        }

        Frame<T> mainFrame = this.mainFrame();

        for (int index = mainFrame.scopes.size() - 1; index >= 0; index--) {
            Map<String, T> currentScope = mainFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return true;
            }
        }

        return false;
    }
}
