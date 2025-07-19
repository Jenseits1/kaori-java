package com.kaori.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Environment<T> {
    public final List<Declaration<T>> declarations;
    public final Stack<Integer> scopeReferences;

    public Environment() {
        this.declarations = new ArrayList<>(1000);
        this.scopeReferences = new Stack<>();

        this.enterScope();
    }

    public T get(int distance) {
        int reference = this.declarations.size() - distance;
        Declaration<T> declaration = declarations.get(reference);

        return declaration.value;
    }

    public void define(String identifier, T value, int distance) {
        if (distance == 0) {
            Declaration<T> declaration = new Declaration<>(identifier, value);
            this.declarations.add(declaration);
        } else {
            int reference = this.declarations.size() - distance;
            Declaration<T> declaration = this.declarations.get(reference);
            declaration.value = value;
        }
    }

    public int searchInner(String identifier) {
        int reference = this.scopeReferences.peek();

        for (int i = declarations.size() - 1; i >= reference; i--) {
            Declaration<T> declaration = declarations.get(i);

            if (declaration.identifier.equals(identifier)) {
                int distance = this.declarations.size() - i;

                return distance;
            }
        }

        return 0;
    }

    public int search(String identifier) {
        for (int i = declarations.size() - 1; i >= 0; i--) {
            Declaration<T> declaration = declarations.get(i);

            if (declaration.identifier.equals(identifier)) {
                int distance = this.declarations.size() - i;

                return distance;
            }
        }

        return 0;
    }

    public void enterScope() {
        int reference = this.declarations.size();

        this.scopeReferences.add(reference);
    }

    public void exitScope() {
        int reference = this.scopeReferences.peek();

        while (this.declarations.size() > reference) {
            System.out.println(declarations.get(declarations.size() - 1));
            this.declarations.remove(declarations.size() - 1);
        }

        this.scopeReferences.pop();
    }
}
