package com.kaori;

import java.util.HashMap;
import java.util.Map;

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
        return previous;
    }

    public void declare(String identifier, Object value, int line) {
        if (values.containsKey(identifier)) {
            throw KaoriError.VariableError(identifier + " is already declared", line);
        }

        values.put(identifier, value);
    }

    public Object get(String identifier, int line) {
        if (values.containsKey(identifier)) {
            return values.get(identifier);
        } else if (previous == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        } else {
            return previous.get(identifier, line);
        }
    }

    public void assign(String identifier, Object value, int line) {
        if (values.containsKey(identifier)) {
            values.put(identifier, value);
        } else if (previous == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        } else {
            previous.assign(identifier, value, line);
        }

    }
}
