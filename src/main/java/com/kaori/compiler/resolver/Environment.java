package com.kaori.compiler.resolver;

import java.util.Stack;

public class Environment {
    private final Stack<Declaration> declarations;
    private int index;
    private Stack<Integer> scopes;
    private int currentFrame;

    public Environment() {
        this.declarations = new Stack<>();
        this.index = 0;
        this.scopes = new Stack<>();
        this.currentFrame = 0;

        this.declarations.setSize(1_000);
        this.scopes.push(0);
    }

    public ResolutionStatus status(DeclarationRef reference) {
        return reference == null ? ResolutionStatus.UNRESOLVED : ResolutionStatus.RESOLVED;
    }

    public void declare(String identifier) {
        int offset = this.index - this.currentFrame;
        boolean local = this.currentFrame > 0;

        DeclarationRef reference = new DeclarationRef(offset, local);
        Declaration declaration = new Declaration(identifier, reference);

        this.declarations.set(this.index, declaration);

        this.index++;
    }

    public DeclarationRef searchInner(String identifier) {
        int top = this.index - 1;

        for (int index = top; index >= this.scopes.peek(); index--) {
            Declaration declaration = declarations.get(index);

            if (declaration.identifier().equals(identifier)) {
                return declaration.reference();
            }
        }

        return null;
    }

    public DeclarationRef search(String identifier) {
        int top = this.index - 1;

        for (int index = top; index >= 0; index--) {
            Declaration declaration = declarations.get(index);

            if (declaration.identifier().equals(identifier)) {
                return declaration.reference();
            }
        }

        return null;
    }

    public void enterScope() {
        this.scopes.add(this.index);
    }

    public void exitScope() {
        this.index = this.scopes.pop();
    }

    public void enterFunction() {
        this.currentFrame = this.index;

        this.enterScope();
    }

    public void exitFunction() {
        this.currentFrame = 0;

        this.exitScope();
    }
}
