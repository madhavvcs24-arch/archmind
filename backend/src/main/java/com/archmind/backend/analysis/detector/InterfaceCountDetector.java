package com.archmind.backend.analysis.detector;

import java.util.regex.Pattern;

public class InterfaceCountDetector {

    private static final Pattern INTERFACE_PATTERN =
            Pattern.compile("\\binterface\\s+\\w+");

    public boolean isInterface(String source) {
        return INTERFACE_PATTERN.matcher(source).find();
    }
}