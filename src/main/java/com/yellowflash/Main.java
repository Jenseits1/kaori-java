package com.yellowflash;

import com.yellowflash.lexer.Lexer;

public class Main {
    public static void main(String[] args) {
        String source = "!=&&!=!";

        Lexer lexer = new Lexer(source);
        lexer.start();

        System.out.println(lexer.tokens);
    }
}