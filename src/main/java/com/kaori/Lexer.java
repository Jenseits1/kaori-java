package com.kaori;

import java.util.ArrayList;
import java.util.List;

import com.kaori.token.Token;
import com.kaori.token.TokenKind;

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
        this.current++;

        if (this.current >= this.source.length()) {
            this.currentCharacter = '\0';
            return;
        }

        this.currentCharacter = this.source.charAt(this.current);

        if (this.currentCharacter == '\n' || this.currentCharacter == '\t') {
            this.line++;
        }
    }

    private boolean fileAtEnd() {
        return this.current >= this.source.length();
    }

    private void addToken(TokenKind type) {
        Token token = new Token(type, this.line, this.start, this.current);

        tokens.add(token);
    }

    private TokenKind number() {
        while (!fileAtEnd() && Character.isDigit(this.currentCharacter)) {
            advance();
        }

        if (this.currentCharacter == '.') {
            advance();
        }

        while (!fileAtEnd() && Character.isDigit(this.currentCharacter)) {
            advance();
        }

        return TokenKind.FLOAT_LITERAL;
    }

    private TokenKind identifierOrKeyword() {
        while (!fileAtEnd() && Character.isAlphabetic(this.currentCharacter)) {
            advance();
        }

        while (!fileAtEnd() && Character.isLetterOrDigit(this.currentCharacter)) {
            advance();
        }

        return switch (this.source.substring(this.start, this.current)) {
            case "make" -> TokenKind.VARIABLE;
            case "if" -> TokenKind.IF;
            case "else" -> TokenKind.ELSE;
            case "while" -> TokenKind.WHILE;
            case "for" -> TokenKind.FOR;
            case "break" -> TokenKind.BREAK;
            case "continue" -> TokenKind.CONTINUE;
            case "return" -> TokenKind.RETURN;
            case "func" -> TokenKind.FUNCTION_LITERAL;
            case "print" -> TokenKind.PRINT;
            case "true", "false" -> TokenKind.BOOLEAN_LITERAL;
            default -> TokenKind.IDENTIFIER;
        };
    }

    private TokenKind stringLiteral() {
        advance();

        while (!fileAtEnd() && this.currentCharacter != '"') {
            advance();
        }

        if (this.currentCharacter != '"') {
            throw KaoriError.SyntaxError("expected closing quotation marks", this.line);
        }

        advance();

        return TokenKind.STRING_LITERAL;
    }

    private TokenKind symbol() {
        String lookahead = this.source.substring(this.current);

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

        TokenKind type = switch (this.currentCharacter) {
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
            default -> throw KaoriError.SyntaxError("unexpected token", this.line);
        };

        advance();

        return type;
    }

    private void reset() {
        this.start = 0;
        this.current = 0;
        this.line = 1;
        this.currentCharacter = this.source.charAt(0);
        tokens = new ArrayList<>();
    }

    private void start() {
        while (!fileAtEnd()) {
            if (Character.isWhitespace(this.currentCharacter)) {
                advance();
            } else if (Character.isDigit(this.currentCharacter)) {
                TokenKind token = number();
                addToken(token);
            } else if (Character.isLetter(this.currentCharacter)) {
                TokenKind token = identifierOrKeyword();
                addToken(token);
            } else if (this.currentCharacter == '"') {
                TokenKind token = stringLiteral();
                addToken(token);
            } else {
                TokenKind token = symbol();
                addToken(token);
            }

            this.start = this.current;
        }
    }

    public List<Token> scan() {
        reset();
        this.start();

        return tokens;
    }

}
