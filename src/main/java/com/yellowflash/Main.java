package com.yellowflash;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yellowflash.ast.statement.Statement;
import com.yellowflash.lexer.Lexer;
import com.yellowflash.lexer.Token;
import com.yellowflash.parser.Parser;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String source = "1 + 2; 2 + 2 * 5;";

        Lexer lexer = new Lexer(source);

        List<Token> tokens = lexer.scan();

        Parser parser = new Parser(tokens);
        List<Statement> ast = parser.parse();

        System.out.println(gson.toJson(ast));

    }
}