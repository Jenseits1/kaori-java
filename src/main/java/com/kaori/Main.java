package com.kaori;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaori.ast.statement.Statement;
import com.kaori.error.SyntaxError;
import com.kaori.interpreter.Interpreter;
import com.kaori.lexer.Lexer;
import com.kaori.lexer.Token;
import com.kaori.parser.Parser;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String source = """
                print(2 + 2);
                {
                    print(3 + 5);
                    print(1.5);

                }
                print("hello world");
                """;

        try {
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scan();

            Parser parser = new Parser(tokens);
            List<Statement> ast = parser.parse();
            Interpreter interpreter = new Interpreter(ast);
            interpreter.run();

        } catch (SyntaxError error) {
            System.out.println(error.getMessage());
        } catch (RuntimeException error) {
            System.out.println(error.getMessage());
        }

    }
}