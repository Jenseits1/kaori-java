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
import com.kaori.visitor.Resolver;
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

            Resolver resolver = new Resolver(ast);
            resolver.run();

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

    private Statement expressionStatement() {
        int line = this.tokens.getLine();

        Expression expression = this.expression();

        return new Statement.Expr(expression).setLine(line);
    }

    private Statement variableStatement() {
        int line = this.tokens.getLine();

        Expression.Identifier left = this.identifier();
        this.tokens.consume(TokenKind.COLON);

        KaoriType type = this.type();

        if (this.tokens.getCurrent() != TokenKind.ASSIGN) {
            Expression right = new Expression.Literal(type, null);
            return new Statement.Variable(left, right, type).setLine(line);
        }

        this.tokens.consume(TokenKind.ASSIGN);

        Expression right = this.expression();

        return new Statement.Variable(left, right, type).setLine(line);
    }

private Statement printStatement() {
    int line = this.tokens.getLine();

    this.tokens.consume(TokenKind.PRINT);

    this.tokens.consume(TokenKind.LEFT_PAREN);

    Expression expression = this.expression();

    this.tokens.consume(TokenKind.RIGHT_PAREN);

    return new Statement.Print(expression).setLine(line);
}
