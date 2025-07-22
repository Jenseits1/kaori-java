package com.kaori.memory;

import java.util.Stack;

public class Environment {
    public final Stack<Declaration> declarations;
    public int scopeDepth;
    public int currentFrame;

    public Environment() {
        this.declarations = new Stack<>();
        this.scopeDepth = 0;
        this.currentFrame = 0;
    }

    public ResolutionStatus get(DeclarationRef reference) {
        if (reference == null) {
            return ResolutionStatus.UNRESOLVED;
        }

        return ResolutionStatus.RESOLVED;
    }

    public void define(String identifier, ResolutionStatus value, DeclarationRef reference) {
        if (reference == null) {
            Declaration declaration = new Declaration(identifier, value, scopeDepth);
            this.declarations.add(declaration);
            return;
        }

        if (reference.local()) {
            int index = this.currentFrame + reference.offset();

            Declaration declaration = this.declarations.get(index);
            declaration.value = value;
        } else {
            int index = reference.offset();

            Declaration declaration = this.declarations.get(index);
            declaration.value = value;
        }
    }

    public DeclarationRef searchInner(String identifier) {
        for (int index = declarations.size() - 1; index >= 0; index--) {
            Declaration declaration = declarations.get(index);

            if (declaration.scopeDepth < this.scopeDepth) {
                break;
            }

            if (declaration.identifier.equals(identifier)) {
                int start = this.currentFrame;
                boolean local = index >= start;
                int offset = local ? index - start : index;

                DeclarationRef reference = new DeclarationRef(offset, local);

                return reference;
            }
        }

        return null;
    }

    public DeclarationRef search(String identifier) {
        for (int index = declarations.size() - 1; index >= 0; index--) {
            Declaration declaration = declarations.get(index);

            if (declaration.identifier.equals(identifier)) {
                int start = this.currentFrame;
                boolean local = index >= start;
                int offset = local ? index - start : index;

                DeclarationRef reference = new DeclarationRef(offset, local);

                return reference;
            }
        }

        return null;
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

    public void enterFunction() {
        this.currentFrame = this.declarations.size();

        this.enterScope();
    }

    public void exitFunction() {
        this.currentFrame = 0;

        this.exitScope();
    }
}
