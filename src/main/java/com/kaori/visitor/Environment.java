package com.kaori.visitor;

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
        return this.previous;
    }

    public Object get(String identifier, int line) {
        if (this.values.containsKey(identifier)) {
            return this.values.get(identifier);
        } else if (this.previous == null) {
            return null;
        } else {
            return this.previous.get(identifier, line);
        }
    }

    public void set(String identifier, Object value) {
        this.values.put(identifier, value);
    }
}
