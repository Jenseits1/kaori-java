package com.kaori.visitor;

import java.util.HashMap;
import java.util.Map;

import com.kaori.parser.Expression;

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

    public Environment<T> find(Expression.Identifier identifier) {
        if (this.values.containsKey(identifier.value) || this.previous == null) {
            return this;
        } else {
            return this.previous.find(identifier);
        }
    }

    public T get(Expression.Identifier identifier) {
        return this.values.get(identifier.value);
    }

    public void set(Expression.Identifier identifier, T value) {
        this.values.put(identifier.value, value);
    }

}
