package com.kaori.parser;

public abstract class KaoriType {

    public static class Primitive extends KaoriType {
        private final String value;
        public static final Primitive STRING = new Primitive("string");
        public static final Primitive NUMBER = new Primitive("number");
        public static final Primitive BOOLEAN = new Primitive("boolean");

        private Primitive(String value) {
            this.value = value;
        }
    }
}
