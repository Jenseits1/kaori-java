package com.kaori.compiler.environment;

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

        for (int i = top; i >= this.scopes.peek(); i--) {
            T declaration = this.declarations.get(i);

            if (declaration.equals(identifier)) {
                boolean local = i >= this.currentFrame;
                int offset = i;

                if (local) {
                    offset = offset - this.currentFrame;
                }

                Resolution resolution = new Resolution(offset, local);

                return resolution;
            }
        }

        return null;
    }

    public Resolution search(String identifier) {
        int top = this.index - 1;

        for (int i = top; i >= 0; i--) {
            T declaration = this.declarations.get(i);

            if (declaration.equals(identifier)) {
                boolean local = i >= this.currentFrame;
                int offset = i;

                if (local) {
                    offset = offset - this.currentFrame;
                }

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

    public boolean insideFunction() {
        return this.currentFrame > 0;
    }
}
