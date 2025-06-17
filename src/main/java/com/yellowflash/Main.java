package com.yellowflash;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yellowflash.lexer.Lexer;
import com.yellowflash.lexer.Token;
import com.yellowflash.parser.Parser;

public class Main {
    public static void main(String[] args) {
        String source = "2 * (3 + 5)";

        Lexer lexer = new Lexer(source);

        List<Token> tokens = lexer.getTokens();

        Parser parser = new Parser(tokens);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println(gson.toJson(parser.execute()));

    }
}