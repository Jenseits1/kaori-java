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
import com.kaori.runtime.Scope;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String source = """
                make number = 0;


                for (make b = 0; b < 5; b = b + 1) {
                    print(b);
                };
                number = "b";
                print(number);
                """;

        try {
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scan();

            Parser parser = new Parser(source, tokens);
            List<Statement> ast = parser.parse();

            Scope scope = new Scope();
            Interpreter interpreter = new Interpreter(ast, scope);
            interpreter.run();
        } catch (KaoriError error) {
            System.out.println(error);
        }

    }
}