package com.kaori;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaori.error.KaoriError;
import com.kaori.lexer.Lexer;
import com.kaori.lexer.Token;
import com.kaori.parser.Parser;
import com.kaori.parser.Statement;
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

            Parser parser = new Parser(source, tokens);
            List<Statement> ast = parser.parse();

            // System.out.println(gson.toJson(ast));

            TypeChecker typeChecker = new TypeChecker(ast);
            typeChecker.run();

            Interpreter interpreter = new Interpreter(ast);
            interpreter.run();

        } catch (KaoriError error) {
            System.out.println(error);
        } catch (IOException error) {
            System.out.println(error);
        }

    }}

Statement statement = switch (this.currentToken.type) {
    case PRINT -> this.printStatement();
    case LEFT_BRACE -> this.blockStatement();
    case IF -> this.ifStatement();
    case WHILE -> this.whileLoopStatement();
    case FOR -> this.forLoopStatement();
    default -> {
        if (lookAhead(TokenKind.IDENTIFIER, TokenKind.COLON)) {
            yield this.variableStatement();
        }

        yield this.expressionStatement();
    }
};