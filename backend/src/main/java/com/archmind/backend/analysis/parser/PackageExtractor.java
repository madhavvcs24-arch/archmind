package com.archmind.backend.analysis.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageExtractor {

    private static final Pattern PACKAGE_PATTERN =
            Pattern.compile("package\\s+([a-zA-Z0-9_.]+);");

    public String extractPackage(String sourceCode) {

        Matcher matcher = PACKAGE_PATTERN.matcher(sourceCode);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "default";
    }
}