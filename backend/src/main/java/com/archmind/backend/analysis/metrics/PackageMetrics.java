package com.archmind.backend.analysis.metrics;

import com.archmind.backend.analysis.graph.ArchitectureGraph;

public class PackageMetrics {

    public int countPackages(ArchitectureGraph graph) {
        return graph.getPackageCount();
    }

}