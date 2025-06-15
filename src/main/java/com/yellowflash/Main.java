package com.yellowflash;

import com.yellowflash.lexer.Lexer;

public class Main {
    public static void main(String[] args) {
        String source = "2 * 5";

        Lexer lexer = new Lexer(source);

        lexer.start();

    }
}