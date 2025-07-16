package com.kaori;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaori.ast.StatementAST;
import com.kaori.error.KaoriError;
import com.kaori.lexer.Lexer;
import com.kaori.parser.Parser;
import com.kaori.token.Token;
import com.kaori.token.TokenStream;
import com.kaori.visitor.Interpreter;
import com.kaori.visitor.Resolver;
import com.kaori.visitor.TypeChecker;
import com.kaori.visitor.Visitor;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Path path = Path.of("src/main/java/com/kaori/source/main.kaori");

            String source = Files.readString(path);

            Lexer lexer = new Lexer(source);

            List<Token> tokens = lexer.scan();

            // System.out.println(gson.toJson(tokens));
            TokenStream tokenStream = new TokenStream(tokens, source);

            Parser parser = new Parser(tokenStream);

            List<StatementAST> ast = parser.parse();

            List<Visitor<?>> visitors = new ArrayList<>();

            visitors.add(new Resolver(ast));
            visitors.add(new TypeChecker(ast));
            visitors.add(new Interpreter(ast));

            for (Visitor<?> visitor : visitors) {
                visitor.run();
            }

        } catch (KaoriError error) {
            System.out.println(error);
        } catch (IOException error) {
            System.out.println(error);
        }
    }

}
