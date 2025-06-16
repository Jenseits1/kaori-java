package com.yellowflash.lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    String source;
    int start;
    int current;
    int line;
    char lookAhead;
    public List<Token> tokens;

    public Lexer(String source) {
        this.source = source;
        this.start = 0;
        this.current = 0;
        this.line = 1;
        this.lookAhead = source.charAt(0);
        this.tokens = new ArrayList<>();
    }

    void advance() {
        current++;

        if (current >= source.length()) {
            lookAhead = '\0';
            return;
        }

        lookAhead = source.charAt(current);

        if (lookAhead == '\n') {
            line++;
        }
    }

    boolean fileAtcurrent() {
        return current >= this.source.length();
    }

    TokenType getNextNumber() {
        while (!fileAtcurrent() && Character.isDigit(lookAhead)) {
            advance();
        }

        if (lookAhead == '.') {
            advance();
        }

        while (!fileAtcurrent() && Character.isDigit(lookAhead)) {
            advance();
        }

        return TokenType.LITERAL;
    }

    TokenType getNextIdentifer() {
        while (!fileAtcurrent() && Character.isAlphabetic(lookAhead)) {
            advance();
        }

        while (!fileAtcurrent() && Character.isLetterOrDigit(lookAhead)) {
            advance();
        }

        return switch (source.substring(start, current)) {
            case "str" -> TokenType.STRING;
            case "float" -> TokenType.FLOAT;
            case "bool" -> TokenType.BOOLEAN;
            case "if" -> TokenType.IF;
            case "else" -> TokenType.ELSE;
            case "while" -> TokenType.WHILE;
            case "for" -> TokenType.FOR;
            case "break" -> TokenType.BREAK;
            case "continue" -> TokenType.CONTINUE;
            case "return" -> TokenType.RETURN;
            case "func" -> TokenType.FUNCTION;
            case "print" -> TokenType.PRINT;
            case "true", "false" -> TokenType.LITERAL;
            default -> TokenType.IDENTIFIER;
        };

    }

    TokenType getNextString() {
        advance();

        while (!fileAtcurrent() && lookAhead != '"') {
            advance();
        }

        if (lookAhead != '"') {
            return TokenType.NOT_FOUND;
        }

        advance();

        return TokenType.LITERAL;
    }

    TokenType getNextTwoCharSymbol() {

        if (current + 2 > source.length()) {
            return TokenType.NOT_FOUND;
        }

        return switch (source.substring(current, current + 2)) {
            case "&&" -> TokenType.AND;
            case "||" -> TokenType.OR;
            case "!=" -> TokenType.NOT_EQUAL;
            case "==" -> TokenType.EQUAL;
            case ">=" -> TokenType.GREATER_EQUAL;
            case "<=" -> TokenType.LESS_EQUAL;
            default -> TokenType.NOT_FOUND;
        };
    }

    TokenType getNextSymbol() {
        TokenType type = getNextTwoCharSymbol();

        if (type != TokenType.NOT_FOUND) {
            advance();
            advance();
            return type;
        }

        type = switch (lookAhead) {
            case '+' -> TokenType.PLUS;
            case '-' -> TokenType.MINUS;
            case '*' -> TokenType.MULTIPLY;
            case '/' -> TokenType.DIVIDE;
            case '%' -> TokenType.REMAINDER;
            case '(' -> TokenType.LEFT_PAREN;
            case ')' -> TokenType.RIGHT_PAREN;
            case '{' -> TokenType.LEFT_BRACE;
            case '}' -> TokenType.RIGHT_BRACE;
            case ',' -> TokenType.COMMA;
            case ';' -> TokenType.SEMICOLON;
            case '!' -> TokenType.NOT;
            case '=' -> TokenType.ASSIGN;
            case '>' -> TokenType.GREATER;
            case '<' -> TokenType.LESS;
            default -> TokenType.NOT_FOUND;
        };

        advance();

        return type;
    }

    public void start() {

        while (!fileAtcurrent()) {
            if (Character.isWhitespace(lookAhead)) {
                advance();
                start = current;
                continue;
            }

            TokenType type;

            if (Character.isDigit(lookAhead)) {
                type = getNextNumber();
            } else if (Character.isLetterOrDigit(lookAhead)) {
                type = getNextIdentifer();
            } else if (lookAhead == '"') {
                type = getNextString();
            } else {
                type = getNextSymbol();
            }

            if (type == TokenType.NOT_FOUND) {

                start = current;
                continue;
            }

            String lexeme = source.substring(start, current);
            Token token = new Token(type, line, lexeme);
            tokens.add(token);

            start = current;
        }
    }
}
