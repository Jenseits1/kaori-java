package com.kaori;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaori.error.KaoriError;
import com.kaori.lexer.Lexer;
import com.kaori.parser.Parser;
import com.kaori.parser.Statement;
import com.kaori.token.Token;
import com.kaori.token.TokenStream;
import com.kaori.visitor.Interpreter;
import com.kaori.visitor.TypeChecker;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Path path = Path.of("src/main/java/com/kaori/source/main.kaori");

            String source = Files.readString(path);

            Lexer lexer = new Lexer(source);

            List<Token> tokens = lexer.scan();

            TokenStream tokenStream = new TokenStream(tokens, source);

            Parser parser = new Parser(tokenStream);

            List<Statement> ast = parser.parse();

            TypeChecker typeChecker = new TypeChecker(ast);
            typeChecker.run();

            Interpreter interpreter = new Interpreter(ast);
            interpreter.run();

        } catch (KaoriError error) {
            System.out.println(error);
        } catch (IOException error) {
            System.out.println(error);
        }

    }

}
