package com.kaori.lexer;

import java.util.ArrayList;
import java.util.List;

import com.kaori.error.KaoriError;
import com.kaori.token.Token;
import com.kaori.token.TokenKind;

public class Lexer {
    private final String source;
    private int index;
    private int current;
    private int line;
    private List<Token> tokens;

    public Lexer(String source) {
        this.source = source;
    }

    private void advance() {
        this.current++;

        if (!this.atEnd() && this.currentChar() == '\n') {
            this.line++;
        }
    }

    private char currentChar() {
        return this.source.charAt(this.current);
    }

    private boolean atEnd() {
        return this.current >= this.source.length();
    }

    private void createToken(TokenKind kind) {
        Token token = new Token(kind, this.line, this.index, this.current);

        this.tokens.add(token);
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

        TokenKind kind = switch (this.source.substring(this.index, this.current)) {
            case "if" -> TokenKind.IF;
            case "else" -> TokenKind.ELSE;
            case "while" -> TokenKind.WHILE;
            case "for" -> TokenKind.FOR;
            case "break" -> TokenKind.BREAK;
            case "continue" -> TokenKind.CONTINUE;
            case "return" -> TokenKind.RETURN;
            case "fun" -> TokenKind.FUNCTION;
            case "print" -> TokenKind.PRINT;
            case "true", "false" -> TokenKind.BOOLEAN_LITERAL;
            default -> TokenKind.IDENTIFIER;
        };

        this.createToken(kind);
    }

    private TokenKind scanStringLiteral() {
        this.advance();

        while (!this.atEnd() && this.currentChar() != '"') {
            this.advance();
        }

        if (!this.atEnd() && this.currentChar() != '"') {
            throw KaoriError.SyntaxError("expected closing quotation marks", this.line);
        }

        this.advance();

        return TokenKind.STRING_LITERAL;
    }

    private TokenKind scanSymbol() {
        String lookahead = this.source.substring(this.current);

        if (lookahead.startsWith("&&")) {
            this.advance();
            this.advance();
            return TokenKind.AND;
        }
        if (lookahead.startsWith("||")) {
            this.advance();
            this.advance();
            return TokenKind.OR;
        }
        if (lookahead.startsWith("!=")) {
            this.advance();
            this.advance();
            return TokenKind.NOT_EQUAL;
        }
        if (lookahead.startsWith("==")) {
            this.advance();
            this.advance();
            return TokenKind.EQUAL;
        }
        if (lookahead.startsWith(">=")) {
            this.advance();
            this.advance();
            return TokenKind.GREATER_EQUAL;
        }
        if (lookahead.startsWith("<=")) {
            this.advance();
            this.advance();
            return TokenKind.LESS_EQUAL;
        }

        if (lookahead.startsWith("++")) {
            this.advance();
            this.advance();
            return TokenKind.INCREMENT;
        }

        if (lookahead.startsWith("--")) {
            this.advance();
            this.advance();
            return TokenKind.DECREMENT;
        }

        TokenKind kind = switch (this.currentChar()) {
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
            case ':' -> TokenKind.COLON;
            case '$' -> TokenKind.DOLLAR;
            case '!' -> TokenKind.NOT;
            case '=' -> TokenKind.ASSIGN;
            case '>' -> TokenKind.GREATER;
            case '<' -> TokenKind.LESS;

            default -> throw KaoriError.SyntaxError("unexpected token", this.line);
        };

        this.advance();

        return kind;
    }

    private void reset() {
        this.index = 0;
        this.current = 0;
        this.line = 1;
        this.tokens = new ArrayList<>();
    }

    private void start() {
        while (!this.atEnd()) {
            if (Character.isWhitespace(this.currentChar())) {
                this.advance();
            } else if (Character.isDigit(this.currentChar())) {
                this.scanNumber();
            } else if (Character.isLetter(this.currentChar())) {
                this.scanIdentifierOrKeyword();

            } else if (this.currentChar() == '"') {
                this.scanStringLiteral();

            } else {
                this.scanSymbol();
            }

            this.index = this.current;
        }
    }

    public List<Token> scan() {
        this.reset();
        this.start();

        return tokens;
    }

}
