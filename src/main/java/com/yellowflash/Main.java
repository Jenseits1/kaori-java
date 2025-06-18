package com.yellowflash;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yellowflash.ast.expression.Expression;
import com.yellowflash.lexer.Lexer;
import com.yellowflash.lexer.Token;
import com.yellowflash.parser.Parser;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String source = "1 + 7 *  3";

        Lexer lexer = new Lexer(source);

        List<Token> tokens = lexer.getTokens();

        Parser parser = new Parser(tokens);
        Expression ast = parser.execute();

        System.out.println(gson.toJson(ast));

    }
}