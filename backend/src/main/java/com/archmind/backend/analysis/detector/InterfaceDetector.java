package com.archmind.backend.analysis.detector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterfaceDetector {

    private static final Pattern IMPLEMENTS_PATTERN =
            Pattern.compile("\\bimplements\\s+([A-Za-z0-9_,\\s]+)");

    public List<String> detectInterfaces(String sourceCode) {

        List<String> interfaces = new ArrayList<>();

        Matcher matcher = IMPLEMENTS_PATTERN.matcher(sourceCode);

        if (matcher.find()) {

            String[] names = matcher.group(1).split(",");

            for (String name : names) {
                interfaces.add(name.trim());
            }
        }

        return interfaces;
    }
}