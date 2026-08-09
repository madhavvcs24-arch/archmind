package com.archmind.backend.analysis.quality;

import java.util.HashMap;
import java.util.Map;

import com.archmind.backend.analysis.dependency.DependencyEdge;
import com.archmind.backend.analysis.graph.ArchitectureGraph;

public class CouplingAnalyzer {

    public Map<String, Integer> calculate(ArchitectureGraph graph) {

        Map<String, Integer> coupling = new HashMap<>();

        for (DependencyEdge edge : graph.getDependencies()) {

            coupling.merge(edge.getSource(), 1, Integer::sum);
        }

        return coupling;
    }
}