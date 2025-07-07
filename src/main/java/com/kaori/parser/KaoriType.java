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
        public boolean equals(KaoriType other) {
            if (other instanceof Primitive o) {
                return this.value == o.value;
            }

            return false;
        }
    }

    public static class FunctionType extends KaoriType {
        private final List<KaoriType> parameters;
        private final KaoriType returnType;

        public FunctionType(List<KaoriType> parameters, KaoriType returnType) {
            this.parameters = parameters;
            this.returnType = parameters;
        }

        @Override
        public boolean equals(KaoriType other) {
            if (other instanceof Primitive o) {
                return this.value == o.value;
            }

            return false;
        }
    }
}
