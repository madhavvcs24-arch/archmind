package com.archmind.backend.analysis.detector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportDetector {

    private static final Pattern IMPORT_PATTERN =
            Pattern.compile("^\\s*import\\s+([a-zA-Z0-9_.*]+);", Pattern.MULTILINE);

    public List<String> detectImports(String sourceCode) {

        List<String> imports = new ArrayList<>();

        Matcher matcher = IMPORT_PATTERN.matcher(sourceCode);

        while (matcher.find()) {
            imports.add(matcher.group(1));
        }

        return imports;
    }
}