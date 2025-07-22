package com.kaori.memory.resolver;

public record Declaration(String identifier, int scopeDepth, DeclarationRef reference) {
}