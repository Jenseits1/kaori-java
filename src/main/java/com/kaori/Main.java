package com.kaori;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        try {
            Path path = Path.of("src/main/java/com/kaori/source/main.kaori");

            String source = Files.readString(path);

        } catch (IOException error) {
            System.out.println(error);
        }
    }

}
