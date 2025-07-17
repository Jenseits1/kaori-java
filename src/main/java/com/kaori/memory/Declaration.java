package com.kaori.memory;

class Declaration<T> {
    public final String identifier;
    public T value;

    public Declaration(String identifier, T value) {
        this.identifier = identifier;
        this.value = value;
    }
}