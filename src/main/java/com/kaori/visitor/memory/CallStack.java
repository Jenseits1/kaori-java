package com.kaori.visitor.memory;

import java.util.HashMap;
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
        HashMap<String, T> scope = new HashMap<>();

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
        HashMap<String, T> currentScope = this.currentFrame().scopes.peek();

        return currentScope.containsKey(identifier);
    }

    public void declare(String identifier) {
        HashMap<String, T> currentScope = this.currentFrame().scopes.peek();

        currentScope.put(identifier, null);
    }

    public T define(String identifier, T value) {
        Frame<T> currentFrame = this.currentFrame();

        int index = currentFrame.scopes.size() - 1;

        while (index >= 0) {
            HashMap<String, T> currentScope = currentFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                currentScope.put(identifier, value);
                return value;
            }

            index--;
        }

        Frame<T> mainFrame = this.mainFrame();

        index = mainFrame.scopes.size() - 1;

        while (index >= 0) {
            HashMap<String, T> currentScope = mainFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                currentScope.put(identifier, value);
                return value;
            }

            index--;
        }

        return null;
    }

    public T get(String identifier) {
        Frame<T> currentFrame = this.currentFrame();

        int index = currentFrame.scopes.size() - 1;

        while (index >= 0) {
            HashMap<String, T> currentScope = currentFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return currentScope.get(identifier);
            }

            index--;
        }

        Frame<T> mainFrame = this.mainFrame();

        index = mainFrame.scopes.size() - 1;

        while (index >= 0) {
            HashMap<String, T> currentScope = mainFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return currentScope.get(identifier);
            }

            index--;
        }

        return null;
    }

    public boolean find(String identifier) {
        Frame<T> currentFrame = this.currentFrame();

        int index = currentFrame.scopes.size() - 1;

        while (index >= 0) {
            HashMap<String, T> currentScope = currentFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return true;
            }

            index--;

        }

        Frame<T> mainFrame = this.mainFrame();

        index = mainFrame.scopes.size() - 1;

        while (index >= 0) {
            HashMap<String, T> currentScope = mainFrame.scopes.get(index);

            if (currentScope.containsKey(identifier)) {
                return true;
            }

            index--;
        }

        return false;
    }
}
