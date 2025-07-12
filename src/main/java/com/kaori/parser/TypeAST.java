package com.kaori.parser;

import java.util.List;

public abstract class TypeAST {
    public abstract boolean equals(TypeAST other);

    public static class Primitive extends TypeAST {
        private final String value;
        public static final Primitive STRING = new Primitive("string");
        public static final Primitive NUMBER = new Primitive("number");
        public static final Primitive BOOLEAN = new Primitive("boolean");

        private Primitive(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @Override
        public boolean equals(TypeAST type) {
            if (type instanceof Primitive other) {
                return this.value == other.value;
            }

            return false;
        }
    }

    public static class Function extends TypeAST {
        private final List<TypeAST> parameters;
        private final TypeAST returnType;

        public Function(List<TypeAST> parameters, TypeAST returnType) {
            this.parameters = parameters;
            this.returnType = returnType;
        }

        @Override
        public String toString() {
            String parameters = String.join(", ",
                    this.parameters.stream().map(parameter -> parameter.toString()).toList());

            return String.format("(%s) => %s", parameters, returnType);
        }

        @Override
        public boolean equals(TypeAST type) {
            if (!(type instanceof Function other)) {
                return false;
            }

            if (!this.returnType.equals(other.returnType)) {
                return false;
            }

            if (this.parameters.size() != other.parameters.size()) {
                return false;
            }

            for (int i = 0; i < this.parameters.size(); i++) {
                TypeAST leftParameter = this.parameters.get(i);
                TypeAST rightParameter = other.parameters.get(i);

                if (!leftParameter.equals(rightParameter)) {
                    return false;
                }
            }

            return true;
        }
    }
}
