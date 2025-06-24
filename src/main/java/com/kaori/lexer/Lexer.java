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

    private void addToken(TokenKind type, int start, int end) {
        Token token = new Token(type, line, start, end);

        tokens.add(token);
    }

    private void getNextNumber() {
        while (!fileAtEnd() && Character.isDigit(currentCharacter)) {
            advance();
        }

        if (currentCharacter == '.') {
            advance();
        }

        while (!fileAtEnd() && Character.isDigit(currentCharacter)) {
            advance();
        }

        addToken(TokenKind.FLOAT_LITERAL, start, current);
    }

    private void identifierOrKeyword() {
        while (!fileAtEnd() && Character.isAlphabetic(currentCharacter)) {
            advance();
        }

        while (!fileAtEnd() && Character.isLetterOrDigit(currentCharacter)) {
            advance();
        }

        TokenKind type = switch (source.substring(start, current)) {
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

        addToken(type, start, current);
    }

    private void stringLiteral() {
        advance();

        while (!fileAtEnd() && currentCharacter != '"') {
            advance();
        }

        if (currentCharacter != '"') {
            throw KaoriError.SyntaxError("expected closing quotation marks", line);
        }

        advance();

        addToken(TokenKind.STRING_LITERAL, start + 1, current - 1);
    }

    private void symbol() {
        String lookahead = source.substring(current);

        if (lookahead.startsWith("&&")) {
            advance();
            advance();
            addToken(TokenKind.AND, start, current);

        } else if (lookahead.startsWith("||")) {
            advance();
            advance();
            addToken(TokenKind.OR, start, current);

        } else if (lookahead.startsWith("!=")) {
            advance();
            advance();
            addToken(TokenKind.NOT_EQUAL, start, current);

        } else if (lookahead.startsWith("==")) {
            advance();
            advance();
            addToken(TokenKind.EQUAL, start, current);

        } else if (lookahead.startsWith(">=")) {
            advance();
            advance();
            addToken(TokenKind.GREATER_EQUAL, start, current);

        } else if (lookahead.startsWith("<=")) {
            advance();
            advance();
            addToken(TokenKind.LESS_EQUAL, start, current);

        } else {
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
            addToken(type, start, current);
        }
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
                getNextNumber();
            } else if (Character.isLetter(currentCharacter)) {
                identifierOrKeyword();
            } else if (currentCharacter == '"') {
                stringLiteral();
            } else {
                symbol();
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
