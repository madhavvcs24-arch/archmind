package com.archmind.backend.analysis.model;

import java.util.List;

public class CircularDependency {

    private final List<String> cycle;

    public CircularDependency(List<String> cycle) {
        this.cycle = cycle;
    }

    public List<String> getCycle() {
        return cycle;
    }
}