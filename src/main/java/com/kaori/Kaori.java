package com.kaori;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.kaori.ast.DeclarationAST;
import com.kaori.error.KaoriError;
import com.kaori.lexer.Lexer;
import com.kaori.parser.Parser;
import com.kaori.token.Token;
import com.kaori.token.TokenStream;
import com.kaori.visitor.Interpreter;
import com.kaori.visitor.Resolver;
import com.kaori.visitor.TypeChecker;
import com.kaori.visitor.Visitor;

public class Kaori {
    public Kaori(String source) {
        try {
            Lexer lexer = new Lexer(source);

            List<Token> tokens = lexer.scan();

            // System.out.println(gson.toJson(tokens));
            TokenStream tokenStream = new TokenStream(tokens, source);

            Parser parser = new Parser(tokenStream);

            List<DeclarationAST> ast = parser.parse();

            List<Visitor<?>> visitors = new ArrayList<>();

            visitors.add(new Resolver(ast));
            visitors.add(new TypeChecker(ast));
            visitors.add(new Interpreter(ast));

            for (Visitor<?> visitor : visitors) {
                visitor.run();
            }

        } catch (KaoriError error) {
            System.out.println(error);
        }
    }
}
