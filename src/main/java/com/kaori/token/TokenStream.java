package com.kaori.token;

import java.util.List;

public class TokenStream {
    private final List<Token> tokens;
    private int currentIndex;
    private int line;
    private Token currentToken;
}
