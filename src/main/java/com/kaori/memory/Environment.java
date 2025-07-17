package com.kaori.memory;

import java.util.Stack;

import com.kaori.error.KaoriError;

public class Environment<T> {
    public final Stack<Declaration<T>> declarations;

    public Environment() {
        this.declarations = new Stack<>();
    }

    public T get(int reference) {
        Declaration<T> declaration = declarations.get(reference);

        return declaration.value;
    }

    public void update(T value, int reference) {
        Declaration<T> declaration = declarations.get(reference);

        declaration.value = value;
    }

    public void add(String identifier, T value) {
        Declaration<T> declaration = new Declaration<>(identifier, value);

        this.declarations.add(declaration);
    }

    public int getReference(String identifier, int line) {
        for (int i = declarations.size() - 1; i >= 0; i--) {
            Declaration<T> declaration = declarations.get(i);

            if (declaration.identifier == identifier) {
                return i;
            }
        }

        throw KaoriError.ResolveError(identifier + " is not declared", line);
    }

    public void enterScope() {

    }

    public void exitScope() {

    }
}
