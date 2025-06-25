package com.kaori;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaori.ast.Statement;
import com.kaori.token.Token;
import com.kaori.visitor.Interpreter;
import com.kaori.visitor.TypeChecker;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String source = """

                print(5 - "a");
                """;

        try {
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scan();

            Parser parser = new Parser(source, tokens);
            List<Statement> ast = parser.parse();

            TypeChecker typeChecker = new TypeChecker(ast);
            typeChecker.run();

            // Interpreter interpreter = new Interpreter(ast);
            // interpreter.run();
        } catch (KaoriError error) {
            System.out.println(error);
        }

    }
}