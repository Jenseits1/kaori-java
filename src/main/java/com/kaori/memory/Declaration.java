package com.kaori.memory;

public class Declaration {
    public final String identifier;
    public ResolutionStatus value;
    public final int scopeDepth;

    public Declaration(String identifier, ResolutionStatus value, int scopeDepth) {
        this.identifier = identifier;
        this.value = value;
        this.scopeDepth = scopeDepth;
    }
}