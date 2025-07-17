package com.kaori.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.kaori.ast.ExpressionAST;

public class Environment<T> {
    public final Stack<Declaration<T>> declarations;

    private static class Declaration<T> {
        public final String identifier;
        public T value;

        public Declaration(String identifier, T value) {
            this.identifier = identifier;
            this.value = value;
        }
    }

    public Environment() {
        this.environments = new Stack<>();
        Map<String, T> environment = new HashMap<>();
        this.environments.push(environment);
    }

    public T get(ExpressionAST.Identifier identifier, int distance) {
        int current = this.environments.size() - 1;
        Map<String, T> environment = environments.get(current - distance);

        return environment.get(identifier.name());
    }

    public void put(ExpressionAST.Identifier identifier, T value) {
        Map<String, T> environment = environments.peek();
        environment.put(identifier.name(), value);
    }

    public void put(ExpressionAST.Identifier identifier, T value, int distance) {
        int current = this.environments.size() - 1;
        Map<String, T> environment = environments.get(current - distance);

        environment.put(identifier.name(), value);
    }

    public int reference(String identifier) {
        for (int i = declarations.size() - 1; i >= 0; i--) {
            Declaration<T> declaration = declarations.get(i);

            if (declaration.identifier == identifier) {
                return i;
            }
        }

        return -1;
    }

    public void enterScope() {
        Map<String, T> environment = new HashMap<>();
        this.environments.add(environment);
    }

    public void exitScope() {
        this.environments.pop();
    }
}
