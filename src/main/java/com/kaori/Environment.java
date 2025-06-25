package com.kaori;

import java.util.HashMap;
import java.util.Map;

public class Environment<T> {
    private final Environment<T> previous;
    private final Map<String, T> values;

    public Environment() {
        this.previous = null;
        this.values = new HashMap<>();
    }

    public Environment(Environment<T> Environment) {
        this.previous = Environment;
        this.values = new HashMap<>();
    }

    public Environment<T> getPrevious() {
        return previous;
    }

    public void declare(String identifier, T value, int line) {
        if (values.containsKey(identifier)) {
            throw KaoriError.VariableError(identifier + " is already declared", line);
        }

        values.put(identifier, value);
    }

    public T get(String identifier, int line) {
        if (values.containsKey(identifier)) {
            return values.get(identifier);
        } else if (previous == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        } else {
            return previous.get(identifier, line);
        }
    }

    public void assign(String identifier, T value, int line) {
        if (values.containsKey(identifier)) {
            values.put(identifier, value);
        } else if (previous == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        } else {
            previous.assign(identifier, value, line);
        }

    }
}
