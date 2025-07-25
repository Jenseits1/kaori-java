package com.kaori;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaori.ast.DeclarationAST;
import com.kaori.compiler.BytecodeGenerator;
import com.kaori.compiler.TypeChecker;
import com.kaori.compiler.resolver.Resolver;
import com.kaori.error.KaoriError;
import com.kaori.lexer.Lexer;
import com.kaori.parser.Parser;
import com.kaori.token.Token;
import com.kaori.token.TokenStream;
import com.kaori.treewalk.Interpreter;
import com.kaori.vm.VirtualMachine;

public class Kaori {
    private final String source;
    private TokenStream tokens;
    private List<DeclarationAST> ast;

    public Kaori(String source) {
        this.source = source;
    }

    public void start() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {

            this.setTokens();
            this.setAst();

            Resolver resolver = new Resolver(ast);
            resolver.run();

            TypeChecker typeChecker = new TypeChecker(ast);
            typeChecker.run();

            /*
             * Interpreter interpreter = new Interpreter(ast);
             * interpreter.run();
             */

            BytecodeGenerator bytecode = new BytecodeGenerator(ast);

            // System.out.println(gson.toJson(bytecode.generateBytecode()));

            VirtualMachine vm = new VirtualMachine(bytecode.generateBytecode());
            vm.run();

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
