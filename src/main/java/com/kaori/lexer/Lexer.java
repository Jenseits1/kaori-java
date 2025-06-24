package com.kaori.lexer;

import java.util.ArrayList;
import java.util.List;
import com.kaori.error.KaoriError;

public class Lexer {
    private final String source;
    private int start;
    private int current;
    private int line;
    private char currentCharacter;
    private List<Token> tokens;

    public Lexer(String source) {
        this.source = source;
    }

    private void advance() {
        current++;

        if (current >= source.length()) {
            currentCharacter = '\0';
            return;
        }

        currentCharacter = source.charAt(current);

        if (currentCharacter == '\n') {
            line++;
        }
    }

    private boolean fileAtEnd() {
        return current >= source.length();
    }

    private void addToken(TokenKind type) {
        Token token = new Token(type, line, start, current);

        tokens.add(token);
    }

    private TokenKind number() {
        while (!fileAtEnd() && Character.isDigit(currentCharacter)) {
            advance();
        }

        if (currentCharacter == '.') {
            advance();
        }

        while (!fileAtEnd() && Character.isDigit(currentCharacter)) {
            advance();
        }

        return TokenKind.FLOAT_LITERAL;
    }

    private TokenKind identifierOrKeyword() {
        while (!fileAtEnd() && Character.isAlphabetic(currentCharacter)) {
            advance();
        }

        while (!fileAtEnd() && Character.isLetterOrDigit(currentCharacter)) {
            advance();
        }

        return switch (source.substring(start, current)) {
            case "str" -> TokenKind.STRING_VARIABLE;
            case "bool" -> TokenKind.BOOLEAN_VARIABLE;
            case "float" -> TokenKind.FLOAT_VARIABLE;
            case "if" -> TokenKind.IF;
            case "else" -> TokenKind.ELSE;
            case "while" -> TokenKind.WHILE;
            case "for" -> TokenKind.FOR;
            case "break" -> TokenKind.BREAK;
            case "continue" -> TokenKind.CONTINUE;
            case "return" -> TokenKind.RETURN;
            case "func" -> TokenKind.FUNCTION;
            case "print" -> TokenKind.PRINT;
            case "true", "false" -> TokenKind.BOOLEAN_LITERAL;
            default -> TokenKind.IDENTIFIER;
        };
    }

    private TokenKind stringLiteral() {
        advance();

        while (!fileAtEnd() && currentCharacter != '"') {
            advance();
        }

        if (currentCharacter != '"') {
            throw KaoriError.SyntaxError("expected closing quotation marks", line);
        }

        advance();

        return TokenKind.STRING_LITERAL;
    }

    private TokenKind symbol() {
        String lookahead = source.substring(current);

        if (lookahead.startsWith("&&")) {
            advance();
            advance();
            return TokenKind.AND;
        }
        if (lookahead.startsWith("||")) {
            advance();
            advance();
            return TokenKind.OR;
        }
        if (lookahead.startsWith("!=")) {
            advance();
            advance();
            return TokenKind.NOT_EQUAL;
        }
        if (lookahead.startsWith("==")) {
            advance();
            advance();
            return TokenKind.EQUAL;
        }
        if (lookahead.startsWith(">=")) {
            advance();
            advance();
            return TokenKind.GREATER_EQUAL;
        }
        if (lookahead.startsWith("<=")) {
            advance();
            advance();
            return TokenKind.LESS_EQUAL;
        }

        TokenKind type = switch (currentCharacter) {
            case '+' -> TokenKind.PLUS;
            case '-' -> TokenKind.MINUS;
            case '*' -> TokenKind.MULTIPLY;
            case '/' -> TokenKind.DIVIDE;
            case '%' -> TokenKind.MODULO;
            case '(' -> TokenKind.LEFT_PAREN;
            case ')' -> TokenKind.RIGHT_PAREN;
            case '{' -> TokenKind.LEFT_BRACE;
            case '}' -> TokenKind.RIGHT_BRACE;
            case ',' -> TokenKind.COMMA;
            case ';' -> TokenKind.SEMICOLON;
            case '!' -> TokenKind.NOT;
            case '=' -> TokenKind.ASSIGN;
            case '>' -> TokenKind.GREATER;
            case '<' -> TokenKind.LESS;
            default -> throw KaoriError.SyntaxError("unexpected token", line);
        };

        advance();

        return type;
    }

    private void reset() {
        start = 0;
        current = 0;
        line = 1;
        currentCharacter = source.charAt(0);
        tokens = new ArrayList<>();
    }

    private void start() {
        while (!fileAtEnd()) {
            if (Character.isWhitespace(currentCharacter)) {
                advance();
            } else if (Character.isDigit(currentCharacter)) {
                TokenKind token = number();
                addToken(token);
            } else if (Character.isLetter(currentCharacter)) {
                TokenKind token = identifierOrKeyword();
                addToken(token);
            } else if (currentCharacter == '"') {
                TokenKind token = stringLiteral();
                addToken(token);
            } else {
                TokenKind token = symbol();
                addToken(token);
            }

            start = current;
        }
    }

    public List<Token> scan() {
        reset();
        start();

        return tokens;
    }

}
