package com.kaori.memory;

import java.util.Stack;

public class Environment<T> {
    public final Stack<Declaration<T>> declarations;
    public int scopeDepth;

    public Environment() {
        this.declarations = new Stack<>();

        this.scopeDepth = 0;
    }

    public T get(int distance) {
        int reference = this.declarations.size() - distance;
        Declaration<T> declaration = declarations.get(reference);

        return declaration.value;
    }

    public void define(String identifier, T value, int distance) {
        if (distance == 0) {
            Declaration<T> declaration = new Declaration<>(identifier, value, this.scopeDepth);
            this.declarations.add(declaration);
        } else {
            int reference = this.declarations.size() - distance;
            Declaration<T> declaration = this.declarations.get(reference);
            declaration.value = value;
        }
    }

    public int searchInner(String identifier) {
        for (int i = declarations.size() - 1; i >= 0; i--) {
            Declaration<T> declaration = declarations.get(i);

            if (declaration.scopeDepth < this.scopeDepth) {
                break;
            }

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
        this.scopeDepth++;
    }

    public void exitScope() {
        while (!this.declarations.empty() && this.declarations.peek().scopeDepth == this.scopeDepth) {
            this.declarations.pop();
        }

        this.scopeDepth--;
    }
}
