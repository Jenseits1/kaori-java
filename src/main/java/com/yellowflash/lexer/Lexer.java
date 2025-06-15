package com.yellowflash.lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexer {
    String source;
    Iterator<Character> current;
    char lookAhead;
    int line = 0;
    List<Token> tokens;

    public Lexer(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
    }

    void advance() {
        lookAhead = current.next();

        if (lookAhead == '\n') {
            line += 1;
        }
    }

    void getNextNumber() {
        StringBuilder number = new StringBuilder();

        while (Character.isDigit(lookAhead)) {
            number.append(lookAhead);
            advance();
        }

        if (lookAhead != '.') {
            Token token = new Token(TokenType.FLOAT, line, number.toString());
            tokens.add(token);
            return;
        }

        number.append(lookAhead);

        while (Character.isDigit(lookAhead)) {
            number.append(lookAhead);
            advance();
        }

        Token token = new Token(TokenType.FLOAT, line, number.toString());
        tokens.add(token);
    }

    void getNextIdentifer() {
        StringBuilder identifier = new StringBuilder();

        while (Character.isAlphabetic(lookAhead)) {
            identifier.append(lookAhead);
            advance();
        }

        while (Character.isLetterOrDigit(lookAhead)) {
            identifier.append(lookAhead);
            advance();
        }

        
        switch (identifier.toString()) {
            case ""
        }
    }

    public void start() {
        List<Character> chars = new ArrayList<>();

        for (char c : source.toCharArray()) {
            chars.add(c);
        }

        current = chars.iterator();

        while (current.hasNext()) {
            advance();


        }

    }
}
