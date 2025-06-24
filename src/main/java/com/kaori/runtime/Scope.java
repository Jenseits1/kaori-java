package com.kaori.runtime;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private Scope outerScope;
    private Map<String, Object> values;

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

    public Object get(String identifier) {
        if (values.containsKey(identifier)) {
            return values.get(identifier);
        }

        if (outerScope == null) {
            return null;
        }

        return outerScope.get(identifier);
    }

    public void declare(String identifier, Object value) {
        values.put(identifier, value);
    }

    public void assign(String identifier, Object value) {
        if (values.containsKey(identifier)) {
            values.put(identifier, value);
        } else if (outerScope != null) {
            outerScope.assign(identifier, value);
        }
    }
}
