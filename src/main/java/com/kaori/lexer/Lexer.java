package com.kaori.lexer;

import java.util.ArrayList;
import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.token.Token;
import com.kaori.token.TokenKind;

public class Lexer {
    private final String source;
    private int left;
    private int right;
    private int line;
    private List<Token> tokens;

    public Lexer(String source) {
        this.source = source;
    }

    private void advance() {
        this.right++;

        if (!this.atEnd() && this.currentChar() == '\n') {
            this.line++;
        }
    }

    private void advance(int steps) {
        for (int i = 0; i < steps; i++) {
            this.right++;

            if (!this.atEnd() && this.currentChar() == '\n') {
                this.line++;
            }
        }
    }

    private char currentChar() {
        return this.source.charAt(this.right);
    }

    private boolean atEnd() {
        return this.right >= this.source.length();
    }

    private boolean lookAhead(String expected) {
        for (int i = 0; i < expected.length(); i++) {
            int j = this.left + i;

            if (j >= this.source.length()) {
                return false;
            }

            if (expected.charAt(i) != this.source.charAt(j)) {
                return false;
            }
        }

        return true;
    }

    private void createToken(TokenKind kind) {
        int position = this.left;
        int size = this.right - this.left;

        Token token = new Token(kind, this.line, position, size);

        this.tokens.add(token);

        this.left = this.right;
    }

    private void scanWhiteSpace() {
        while (!this.atEnd() && Character.isWhitespace(this.currentChar())) {
            this.advance();
        }

        this.left = this.right;
    }

    private void scanNumber() {
        while (!this.atEnd() && Character.isDigit(this.currentChar())) {
            this.advance();
        }

        if (!this.atEnd() && this.currentChar() == '.') {
            this.advance();
        }

        while (!this.atEnd() && Character.isDigit(this.currentChar())) {
            this.advance();
        }

        this.createToken(TokenKind.NUMBER_LITERAL);
    }

    private void scanIdentifierOrKeyword() {
        while (!this.atEnd() && Character.isAlphabetic(this.currentChar())) {
            this.advance();
        }

        while (!this.atEnd()
                && (Character.isLetterOrDigit(this.currentChar()) || this.currentChar() == '_')) {
            this.advance();
        }

        TokenKind kind = switch (this.source.substring(this.left, this.right)) {
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

        this.createToken(kind);
    }

    private void scanStringLiteral() {
        int line = this.line;

        this.advance();

        while (!this.atEnd() && this.currentChar() != '"') {
            this.advance();
        }

        if (this.atEnd() || this.currentChar() != '"') {
            throw KaoriError.SyntaxError("missing closing quotation marks for string literal", line);
        }

        this.advance();

        this.createToken(TokenKind.STRING_LITERAL);
    }

    private void scanSymbol() {
        TokenKind kind = switch (this.currentChar()) {
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
            throw KaoriError.SyntaxError("invalid token " + this.currentChar(), this.line);
        }

        int symbolSize = switch (kind) {
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

        this.advance(symbolSize);

        this.createToken(kind);
    }

    private void reset() {
        this.left = 0;
        this.right = 0;
        this.line = 1;
        this.tokens = new ArrayList<>();
    }

    private void start() {
        while (!this.atEnd()) {
            if (Character.isWhitespace(this.currentChar())) {
                this.scanWhiteSpace();
            } else if (Character.isDigit(this.currentChar())) {
                this.scanNumber();
            } else if (Character.isLetter(this.currentChar())) {
                this.scanIdentifierOrKeyword();
            } else if (this.currentChar() == '"') {
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
