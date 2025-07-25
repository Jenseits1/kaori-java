package com.kaori.compiler.resolver;

import java.util.Stack;

public class Environment<T> {
    private final Stack<T> declarations;
    private int index;
    private Stack<Integer> scopes;
    private int currentFrame;

    public Environment() {
        this.declarations = new Stack<>();
        this.index = 0;
        this.scopes = new Stack<>();
        this.currentFrame = 0;

        this.declarations.setSize(1_000);
        this.scopes.push(0);
    }

    public void declare(T value) {
        this.declarations.set(this.index, value);

        this.index++;
    }

    public void define(T value, int offset, boolean local) {
        if (local) {
            offset = this.currentFrame + offset;
        }

        this.declarations.set(offset, value);
    }

    public T get(int offset, boolean local) {
        if (local) {
            offset = this.currentFrame + offset;
        }

        return this.declarations.get(offset);
    }

    public Resolution searchInner(String identifier) {
        int top = this.index - 1;

        for (int index = top; index >= this.scopes.peek(); index--) {
            T declaration = this.declarations.get(index);

            if (declaration.equals(identifier)) {
                int offset = this.index - this.currentFrame;
                boolean local = this.currentFrame > 0;

                Resolution resolution = new Resolution(offset, local);

                return resolution;
            }
        }

        return null;
    }

    public Resolution search(String identifier) {
        int top = this.index - 1;

        for (int index = top; index >= 0; index--) {
            T declaration = this.declarations.get(index);

            if (declaration.equals(identifier)) {
                int offset = this.index - this.currentFrame;
                boolean local = this.currentFrame > 0;

                Resolution resolution = new Resolution(offset, local);

                return resolution;
            }
        }

        return null;
    }

    public void enterScope() {
        this.scopes.add(this.index);
    }

    public void exitScope() {
        this.index = this.scopes.pop();
    }

    public void enterFunction() {
        this.currentFrame = this.index;

        this.enterScope();
    }

    public void exitFunction() {
        this.currentFrame = 0;

        this.exitScope();
    }
}
