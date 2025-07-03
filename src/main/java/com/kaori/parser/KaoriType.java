package com.kaori.parser;

public interface KaoriType {
    public enum Primitive implements KaoriType {
        STRING,
        NUMBER,
        BOOLEAN,
    }

}
