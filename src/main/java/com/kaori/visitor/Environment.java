package com.kaori.visitor;

import java.util.HashMap;
import java.util.Map;

import com.kaori.error.KaoriError;

public class Environment<T> {
    private final Environment<T> previous;
    private final Map<String, T> values;

    public Environment() {
        this.previous = null;
        this.values = new HashMap<>();
    }

    public Environment(Environment<T> environment) {
        this.previous = environment;
        this.values = new HashMap<>();
    }

    public Environment<T> getPrevious() {
        return this.previous;
    }

    private Environment<T> find(String identifier) {
        if (this.values.containsKey(identifier)) {
            return this;
        } else if (this.previous == null) {
            return null;
        } else {
            return this.previous.find(identifier);
        }
    }

    public T get(String identifier, int line) {
        Environment<T> env = this.find(identifier);

        if (env == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        }

        T value = env.values.get(identifier);

        if (value == null) {
            throw KaoriError.VariableError(identifier + " is null", line);
        }

        return env.values.get(identifier);
    }

    public void assign(String identifier, T value, int line) {
        Environment<T> env = this.find(identifier);

        if (env == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        }

        env.values.put(identifier, value);
    }

    public void declare(String identifier, T value, int line) {
        if (this.values.containsKey(identifier)) {
            throw KaoriError.VariableError(identifier + " is already declared", line);
        }

        this.values.put(identifier, value);
    }
}
