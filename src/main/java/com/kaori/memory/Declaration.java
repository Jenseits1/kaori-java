package com.kaori.memory;

public class Declaration<T> {
    public final String identifier;
    public T value;
    public final int scopeDepth;

    public Declaration(String identifier, T value, int scopeDepth) {
        this.identifier = identifier;
        this.value = value;
        this.scopeDepth = scopeDepth;
    }
}