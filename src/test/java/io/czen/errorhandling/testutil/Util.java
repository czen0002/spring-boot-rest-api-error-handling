package io.czen.errorhandling.testutil;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

public class Util {
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(new ClassPathResource(fileName).getFile().toPath()));
    }
}
