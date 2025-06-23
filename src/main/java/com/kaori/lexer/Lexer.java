package com.kaori.lexer;

import java.util.ArrayList;
import java.util.List;
import com.kaori.error.KaoriError;

public class Lexer {
    String source;
    int start;
    int current;
    int line;
    char currentCharacter;
    List<Token> tokens;

    public Lexer(String source) {
        this.source = source;
    }

    void advance() {
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

    boolean fileAtEnd() {
        return current >= source.length();
    }

    void addToken(TokenType type, int start, int end) {
        String lexeme = source.substring(start, end);
        Token token = new Token(type, line, lexeme);

        tokens.add(token);
    }

    void getNextNumber() {
        while (!fileAtEnd() && Character.isDigit(currentCharacter)) {
            advance();
        }

        if (currentCharacter == '.') {
            advance();
        }

        while (!fileAtEnd() && Character.isDigit(currentCharacter)) {
            advance();
        }

        addToken(TokenType.FLOAT_LITERAL, start, current);
    }

    void identifierOrKeyword() {
        while (!fileAtEnd() && Character.isAlphabetic(currentCharacter)) {
            advance();
        }

        while (!fileAtEnd() && Character.isLetterOrDigit(currentCharacter)) {
            advance();
        }

        TokenType type = switch (source.substring(start, current)) {
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
            case "true", "false" -> TokenType.BOOLEAN_LITERAL;
            default -> TokenType.IDENTIFIER;
        };

        addToken(type, start, current);
    }

    void stringLiteral() {
        advance();

        while (!fileAtEnd() && currentCharacter != '"') {
            advance();
        }

        if (currentCharacter != '"') {
            throw new KaoriError.SyntaxError("expected closing quotation marks", line);
        }

        advance();

        addToken(TokenType.STRING_LITERAL, start, current);
    }

    void symbol() {
        String lookahead = source.substring(current);

        if (lookahead.startsWith("&&")) {
            advance();
            advance();
            addToken(TokenType.AND, start, current);

        } else if (lookahead.startsWith("||")) {
            advance();
            advance();
            addToken(TokenType.OR, start, current);

        } else if (lookahead.startsWith("!=")) {
            advance();
            advance();
            addToken(TokenType.NOT_EQUAL, start, current);

        } else if (lookahead.startsWith("==")) {
            advance();
            advance();
            addToken(TokenType.EQUAL, start, current);

        } else if (lookahead.startsWith(">=")) {
            advance();
            advance();
            addToken(TokenType.GREATER_EQUAL, start, current);

        } else if (lookahead.startsWith("<=")) {
            advance();
            advance();
            addToken(TokenType.LESS_EQUAL, start, current);

        } else {
            TokenType type = switch (currentCharacter) {
                case '+' -> TokenType.PLUS;
                case '-' -> TokenType.MINUS;
                case '*' -> TokenType.MULTIPLY;
                case '/' -> TokenType.DIVIDE;
                case '%' -> TokenType.MODULO;
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
                default -> throw new KaoriError.SyntaxError("unexpected token", line);
            };

            advance();
            addToken(type, start, current);
        }
    }

    void reset() {
        start = 0;
        current = 0;
        line = 1;
        currentCharacter = source.charAt(0);
        tokens = new ArrayList<>();
    }

    void start() {
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
