package com.archmind.backend.analysis.detector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InheritanceDetector {

    private static final Pattern EXTENDS_PATTERN =
            Pattern.compile("\\bextends\\s+([A-Za-z_][A-Za-z0-9_]*)");

    public String detectParentClass(String sourceCode) {

        Matcher matcher = EXTENDS_PATTERN.matcher(sourceCode);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}