package com.kaori;

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
    private final String source;
    private TokenStream tokens;
    private List<DeclarationAST> ast;

    public Kaori(String source) {
        this.source = source;
    }

    public void start() {
        try {

            this.setTokens();
            this.setAst();

            Resolver resolver = new Resolver(ast);
            resolver.run();

            TypeChecker typeChecker = new TypeChecker(ast);
            typeChecker.run();

            Interpreter interpreter = new Interpreter(ast);
            interpreter.run();

        } catch (KaoriError error) {
            System.out.println(error);
        }
    }

    private void setAst() {
        Parser parser = new Parser(this.tokens);

        this.ast = parser.parse();
    }

    private void setTokens() {
        Lexer lexer = new Lexer(this.source);

        List<Token> tokens = lexer.scan();
        this.tokens = new TokenStream(tokens, source);
    }
}
