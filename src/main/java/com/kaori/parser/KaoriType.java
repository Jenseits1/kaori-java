package com.kaori.parser;

import java.util.List;

public abstract class KaoriType {
    public abstract boolean equals(KaoriType other);

    public static class Primitive extends KaoriType {
        private final String value;
        public static final Primitive STRING = new Primitive("string");
        public static final Primitive NUMBER = new Primitive("number");
        public static final Primitive BOOLEAN = new Primitive("boolean");

        private Primitive(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(KaoriType type) {
            if (type instanceof Primitive other) {
                return this.value == other.value;
            }

            return false;
        }
    }

    public static class FunctionType extends KaoriType {
        private final List<KaoriType> parameters;
        private final KaoriType returnType;

        public FunctionType(List<KaoriType> parameters, KaoriType returnType) {
            this.parameters = parameters;
            this.returnType = returnType;
        }

        @Override
        public boolean equals(KaoriType type) {
            if (!(type instanceof FunctionType other)) {
                return false;
            }

            if (!this.returnType.equals(other.returnType)) {
                return false;
            }

            if (this.parameters.size() != other.parameters.size()) {
                return false;
            }

            for (int i = 0; i < this.parameters.size(); i++) {
                KaoriType leftParameter = this.parameters.get(i);
                KaoriType rightParameter = other.parameters.get(i);

                if (!leftParameter.equals(rightParameter)) {
                    return false;
                }
            }

            return true;
        }
    }
}
