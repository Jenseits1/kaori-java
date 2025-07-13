package com.kaori.visitor.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Environment<T> {
    Stack<Map<String, T>> environments;

    public Environment() {
        this.environments = new Stack<>();
        this.environments.push(new HashMap<>());
    }

    public void declare(String identifier) {
        Map<String, T> environment = environments.peek();
        environment.put(identifier, null);
    }

    public boolean declared(String identifier) {
        Map<String, T> environment = environments.peek();

        return environment.containsKey(identifier);
    }

    public T get(String identifier) {
        for (int i = environments.size() - 1; i >= 0; i--) {
            Map<String, T> environment = environments.get(i);

            if (environment.containsKey(identifier)) {
                return environment.get(identifier);
            }
        }

        return null;
    }

    public void define(String identifier, T value) {
        for (int i = environments.size() - 1; i >= 0; i--) {
            Map<String, T> environment = environments.get(i);

            if (environment.containsKey(identifier)) {
                environment.put(identifier, value);
                break;
            }
        }
    }
}
