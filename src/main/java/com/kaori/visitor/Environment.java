package com.kaori.visitor;

import java.util.HashMap;
import java.util.Map;

import com.kaori.error.KaoriError;

public class Environment {
    private final Environment previous;
    private final Map<String, Object> values;

    public Environment() {
        this.previous = null;
        this.values = new HashMap<>();
    }

    public Environment(Environment Environment) {
        this.previous = Environment;
        this.values = new HashMap<>();
    }

    public Environment getPrevious() {
        return this.previous;
    }

    private Environment find(String identifier) {
        if (this.values.containsKey(identifier)) {
            return this;
        } else if (this.previous == null) {
            return null;
        } else {
            return this.previous.find(identifier);
        }
    }

    public Object get(String identifier, int line) {
        Environment env = this.find(identifier);

        if (env == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        }

        Object value = env.values.get(identifier);

        if (value == null) {
            throw KaoriError.VariableError(identifier + " is null", line);
        }

        return env.values.get(identifier);
    }

    public void assign(String identifier, Object value, int line) {
        Environment env = this.find(identifier);

        if (env == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        }

        env.values.put(identifier, value);
    }

    public void declare(String identifier, Object value, int line) {
        if (this.values.containsKey(identifier)) {
            throw KaoriError.VariableError(identifier + " is already declared", line);
        }

        this.values.put(identifier, value);
    }
}
