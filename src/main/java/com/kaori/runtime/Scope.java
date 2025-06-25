package com.kaori.runtime;

import java.util.HashMap;
import java.util.Map;

import com.kaori.error.KaoriError;

public class Scope {
    private Scope outerScope;
    private final Map<String, Object> values;

    public Scope() {
        this.outerScope = null;
        this.values = new HashMap<>();
    }

    public Scope(Scope scope) {
        this.outerScope = scope;
        this.values = new HashMap<>();
    }

    public Scope getOuterScope() {
        return outerScope;
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
        } else if (outerScope == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        } else {
            return outerScope.get(identifier, line);
        }
    }

    public void assign(String identifier, Object value, int line) {
        if (values.containsKey(identifier)) {
            values.put(identifier, value);
        } else if (outerScope == null) {
            throw KaoriError.VariableError(identifier + " is not declared", line);
        } else {
            outerScope.assign(identifier, value, line);
        }

    }
}
