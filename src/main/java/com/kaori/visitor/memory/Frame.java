package com.kaori.visitor.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Frame<T> {
    public final Stack<Map<String, T>> scopes;

    public Frame() {
        this.scopes = new Stack<>();
        Map<String, T> scope = new HashMap<>();

        this.scopes.add(scope);
    }
}