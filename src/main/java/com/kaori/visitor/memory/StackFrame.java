package com.kaori.visitor.memory;

import java.util.HashMap;
import java.util.Stack;

public class StackFrame<T> {
    public final Stack<HashMap<String, T>> environments;

    public StackFrame() {
        this.environments = new Stack<>();

        this.enterScope();
    }

    public void enterScope() {
        HashMap<String, T> environment = new HashMap<>();
        this.environments.add(environment);
    }

    public void leaveScope() {
        HashMap<String, T> environment = new HashMap<>();
        this.environments.add(environment);
    }

    public boolean find(String identifier) {
        int index = this.environments.size() - 1;

        while (index >= 0) {
            HashMap<String, T> environment = this.environments.get(index);

            if (environment.containsKey(identifier)) {
                return true;
            }

            index--;
        }

        return false;
    }

    public boolean isDeclared(String identifier) {
        HashMap<String, T> environment = this.environments.peek();

        return environment.containsKey(identifier);
    }

    public void declare(String identifier) {
        HashMap<String, T> environment = this.environments.peek();

        environment.put(identifier, null);
    }

    public void define(String identifier, T value) {
        int index = 0;

        while (index >= 0) {
            HashMap<String, T> environment = this.environments.get(index);

            if (environment.containsKey(identifier)) {
                environment.put(identifier, value);
                break;
            }

            index--;
        }
    }

    public T get(String identifier) {
        int index = 0;

        while (index >= 0) {
            HashMap<String, T> environment = this.environments.get(index);

            if (environment.containsKey(identifier)) {
                break;
            }

            index--;
        }

        HashMap<String, T> environment = this.environments.get(index);

        return environment.get(identifier);
    }
}
