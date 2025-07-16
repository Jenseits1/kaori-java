package com.kaori.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.kaori.ast.ExpressionAST;

public class Environment<T> {
    public final Stack<Map<String, T>> environments;

    public Environment() {
        this.environments = new Stack<>();
        Map<String, T> environment = new HashMap<>();
        this.environments.push(environment);
    }

    public T get(ExpressionAST.Identifier identifier, int distance) {
        int current = this.environments.size() - 1;
        Map<String, T> environment = environments.get(current - distance);

        return environment.get(identifier.value());
    }

    public void put(ExpressionAST.Identifier identifier, T value) {
        Map<String, T> environment = environments.peek();
        environment.put(identifier.value(), value);
    }

    public void put(ExpressionAST.Identifier identifier, T value, int distance) {
        int current = this.environments.size() - 1;
        Map<String, T> environment = environments.get(current - distance);

        environment.put(identifier.value(), value);
    }

    public int distance(ExpressionAST.Identifier identifier) {
        for (int i = environments.size() - 1; i >= 0; i--) {
            Map<String, T> environment = environments.get(i);

            if (environment.containsKey(identifier.value())) {
                return this.environments.size() - 1 - i;
            }
        }

        return -1;
    }

    public void enterScope() {
        Map<String, T> environment = new HashMap<>();
        this.environments.add(environment);
    }

    public void exitScope() {
        this.environments.pop();
    }
}
