package com.kaori;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaori.ast.Statement;
import com.kaori.error.KaoriError;
import com.kaori.lexer.Lexer;
import com.kaori.lexer.Token;
import com.kaori.parser.Parser;
import com.kaori.runtime.Interpreter;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String source = """
                float number = 0;

                for (float number = 0; number < 10; number = number + 1) {
                    print(number);
                };

                print("hello world");
                """;

        try {
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scan();

            Parser parser = new Parser(source, tokens);
            List<Statement> ast = parser.parse();
            Interpreter interpreter = new Interpreter(ast);
            interpreter.run();
        } catch (KaoriError error) {
            System.out.println(error);
        }

    }
}