package com.archmind.backend.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassExtractor {

    private static final Pattern CLASS_PATTERN =
            Pattern.compile("\\b(class|interface|enum|record)\\s+([A-Za-z_][A-Za-z0-9_]*)");

    public String extractClassName(String sourceCode) {

        Matcher matcher = CLASS_PATTERN.matcher(sourceCode);

        if (matcher.find()) {
            return matcher.group(2);
        }

        return "UnknownClass";
    }
}