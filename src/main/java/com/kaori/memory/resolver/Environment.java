package com.kaori.memory.resolver;

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

    public ResolutionStatus status(DeclarationRef reference) {
        return reference == null ? ResolutionStatus.UNRESOLVED : ResolutionStatus.RESOLVED;
    }

    public void declare(String identifier) {
        int offset = this.declarations.size() - this.currentFrame;
        boolean local = this.currentFrame > 0;
        int scopeDepth = this.scopeDepth;
        DeclarationRef reference = new DeclarationRef(offset, local);
        Declaration declaration = new Declaration(identifier, scopeDepth, reference);

        this.declarations.add(declaration);
    }

    public DeclarationRef searchInner(String identifier) {
        for (int index = declarations.size() - 1; index >= 0; index--) {
            Declaration declaration = declarations.get(index);

            if (declaration.scopeDepth() < this.scopeDepth) {
                break;
            }

            if (declaration.identifier().equals(identifier)) {
                return declaration.reference();
            }
        }

        return null;
    }

    public DeclarationRef search(String identifier) {
        for (int index = declarations.size() - 1; index >= 0; index--) {
            Declaration declaration = declarations.get(index);

            if (declaration.identifier().equals(identifier)) {
                return declaration.reference();
            }
        }

        return null;
    }

    public void enterScope() {
        this.scopeDepth++;
    }

    public void exitScope() {
        while (!this.declarations.empty() && this.declarations.peek().scopeDepth() == this.scopeDepth) {
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
