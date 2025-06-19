package com.kaori.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        String lexeme = source.substring(start, current);
        Token token = new Token(TokenType.FLOAT_LITERAL, line, lexeme);

        tokens.add(token);
    }

    void getNextIdentifer() {
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

        String lexeme = source.substring(start, current);
        Token token = new Token(type, line, lexeme);

        tokens.add(token);
    }

    void getNextString() {
        advance();

        while (!fileAtEnd() && currentCharacter != '"') {
            advance();
        }

        if (currentCharacter != '"') {
            throw new Error("Missing closing quotation marks!");
        }

        advance();

        String lexeme = source.substring(start, current);
        Token token = new Token(TokenType.STRING_LITERAL, line, lexeme);

        tokens.add(token);
    }

    Optional<TokenType> getNextTwoCharSymbol() {
        if (current + 2 > source.length()) {
            return Optional.empty();
        }

        Optional<TokenType> type = switch (source.substring(current, current + 2)) {
            case "&&" -> Optional.of(TokenType.AND);
            case "||" -> Optional.of(TokenType.OR);
            case "!=" -> Optional.of(TokenType.NOT_EQUAL);
            case "==" -> Optional.of(TokenType.EQUAL);
            case ">=" -> Optional.of(TokenType.GREATER_EQUAL);
            case "<=" -> Optional.of(TokenType.LESS_EQUAL);
            default -> Optional.empty();
        };

        if (type.isEmpty()) {
            return Optional.empty();
        }

        advance();
        advance();

        return type;
    }

    void getNextSymbol() {
        Optional<TokenType> twoCharTokenType = getNextTwoCharSymbol();

        if (twoCharTokenType.isPresent()) {
            String lexeme = source.substring(start, current);
            Token token = new Token(twoCharTokenType.get(), line, lexeme);

            tokens.add(token);
            return;
        }

        TokenType type = switch (currentCharacter) {
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
            default -> TokenType.INVALID_TOKEN;
        };

        advance();

        if (type == TokenType.INVALID_TOKEN) {
            return;
        }

        String lexeme = source.substring(start, current);
        Token token = new Token(type, line, lexeme);

        tokens.add(token);
    }

    void reset() {
        start = 0;
        current = 0;
        line = 1;
        currentCharacter = source.charAt(0);
        tokens = new ArrayList<>();
    }

    public List<Token> scan() {
        reset();

        while (!fileAtEnd()) {
            if (Character.isWhitespace(currentCharacter)) {
                advance();
            } else if (Character.isDigit(currentCharacter)) {
                getNextNumber();
            } else if (Character.isLetterOrDigit(currentCharacter)) {
                getNextIdentifer();
            } else if (currentCharacter == '"') {
                getNextString();
            } else {
                getNextSymbol();
            }

            start = current;
        }

        return tokens;
    }

}
