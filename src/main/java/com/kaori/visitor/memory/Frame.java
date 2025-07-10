package com.kaori.visitor.memory;

import java.util.HashMap;
import java.util.Stack;

public class Frame<T> {
    public final Stack<HashMap<String, T>> scopes;

    public Frame() {
        this.scopes = new Stack<>();
        HashMap<String, T> scope = new HashMap<>();

        this.scopes.add(scope);
    }
}