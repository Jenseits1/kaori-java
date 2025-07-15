package com.kaori.lexer;

import java.util.ArrayList;
import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.token.Token;
import com.kaori.token.TokenKind;

public class Lexer {
    private final String source;
    private int index;
    private int line;
    private List<Token> tokens;

    public Lexer(String source) {
        this.source = source;
    }

    private void advance(int steps) {
        for (int i = 0; i < steps; i++) {
            this.index++;

            if (!this.atEnd() && this.source.charAt(this.index) == '\n') {
                this.line++;
            }
        }
    }

    private boolean atEnd(int index) {
        return index >= this.source.length();
    }

    private boolean atEnd() {
        return this.index >= this.source.length();
    }

    private boolean lookAhead(String expected) {
        for (int i = 0; i < expected.length(); i++) {
            int j = this.index + i;

            if (j >= this.source.length()) {
                return false;
            }

            if (expected.charAt(i) != this.source.charAt(j)) {
                return false;
            }
        }

        return true;
    }

    private void createToken(TokenKind kind, int size) {
        int position = this.index;

        Token token = new Token(kind, this.line, position, size);

        this.tokens.add(token);

        this.left = this.index;
    }

    private void scanWhiteSpace() {
        while (!this.atEnd() && Character.isWhitespace(this.source.charAt(this.index))) {
            this.index++;
        }
    }

    private void scanNumber() {
        int current = this.index;

        while (!this.atEnd(current) && Character.isDigit(this.source.charAt(current))) {
            current++;
        }

        if (!this.atEnd(current) && this.source.charAt(current) == '.') {
            current++;
        }

        while (!this.atEnd(current) && Character.isDigit(this.source.charAt(current))) {
            current++;
        }

        int size = current - this.index;

        this.createToken(TokenKind.NUMBER_LITERAL, size);
    }

    private void scanIdentifierOrKeyword() {
        int current = this.index;

        while (!this.atEnd(current) && Character.isAlphabetic(this.source.charAt(current))) {
            current++;
        }

        while (!this.atEnd(current)
                && (Character.isLetterOrDigit(this.source.charAt(current)) || this.source.charAt(current) == '_')) {
            current++;
        }

        TokenKind kind = switch (this.source.substring(this.left, this.index)) {
            case "if" -> TokenKind.IF;
            case "else" -> TokenKind.ELSE;
            case "while" -> TokenKind.WHILE;
            case "for" -> TokenKind.FOR;
            case "break" -> TokenKind.BREAK;
            case "continue" -> TokenKind.CONTINUE;
            case "return" -> TokenKind.RETURN;
            case "def" -> TokenKind.FUNCTION;
            case "print" -> TokenKind.PRINT;
            case "true", "false" -> TokenKind.BOOLEAN_LITERAL;
            default -> TokenKind.IDENTIFIER;
        };

        int size = current - this.index;

        this.createToken(kind, size);
    }

    private void scanStringLiteral() {
        int line = this.line;
        int current = this.index;

        current++;

        while (!this.atEnd(current) && this.source.charAt(current) != '"') {
            current++;
        }

        if (this.atEnd(current) || this.source.charAt(current) != '"') {
            throw KaoriError.SyntaxError("missing closing quotation marks for string literal", line);
        }

        current++;

        int size = current - this.index;

        this.createToken(TokenKind.STRING_LITERAL, size);
    }

    private void scanSymbol() {
        TokenKind kind = switch (this.source.charAt(this.index)) {
            case '+' -> this.lookAhead("++") ? TokenKind.INCREMENT : TokenKind.PLUS;
            case '-' -> this.lookAhead("--") ? TokenKind.DECREMENT : TokenKind.MINUS;
            case '&' -> this.lookAhead("&&") ? TokenKind.AND : TokenKind.INVALID;
            case '|' -> this.lookAhead("||") ? TokenKind.OR : TokenKind.INVALID;
            case '=' -> this.lookAhead("==") ? TokenKind.EQUAL : TokenKind.ASSIGN;
            case '!' -> this.lookAhead("!=") ? TokenKind.NOT_EQUAL : TokenKind.NOT;
            case '>' -> this.lookAhead(">=") ? TokenKind.GREATER_EQUAL : TokenKind.GREATER;
            case '<' -> this.lookAhead("<=") ? TokenKind.LESS_EQUAL : TokenKind.LESS;
            case '*' -> TokenKind.MULTIPLY;
            case '/' -> TokenKind.DIVIDE;
            case '%' -> TokenKind.MODULO;
            case '(' -> TokenKind.LEFT_PAREN;
            case ')' -> TokenKind.RIGHT_PAREN;
            case '{' -> TokenKind.LEFT_BRACE;
            case '}' -> TokenKind.RIGHT_BRACE;
            case ',' -> TokenKind.COMMA;
            case ';' -> TokenKind.SEMICOLON;
            case ':' -> TokenKind.COLON;
            case '$' -> TokenKind.DOLLAR;
            default -> TokenKind.INVALID;
        };

        if (kind == TokenKind.INVALID) {
            throw KaoriError.SyntaxError("invalid token " + this.source.charAt(this.index), this.line);
        }

        int size = switch (kind) {
            case INCREMENT,
                    DECREMENT,
                    AND,
                    OR,
                    NOT_EQUAL,
                    EQUAL,
                    GREATER_EQUAL,
                    LESS_EQUAL ->
                2;
            default -> 1;
        };

        this.createToken(kind, size);
    }

    private void reset() {
        this.left = 0;
        this.index = 0;
        this.line = 1;
        this.tokens = new ArrayList<>();
    }

    private void start() {
        while (!this.atEnd()) {
            char c = this.source.charAt(this.index);

            if (Character.isWhitespace(c)) {
                this.scanWhiteSpace();
            } else if (Character.isDigit(c)) {
                this.scanNumber();
            } else if (Character.isLetter(c)) {
                this.scanIdentifierOrKeyword();
            } else if (c == '"') {
                this.scanStringLiteral();
            } else {
                this.scanSymbol();
            }
        }

    }

    public List<Token> scan() {
        this.reset();
        this.start();

        return tokens;
    }

}
