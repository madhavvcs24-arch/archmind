package com.archmind.backend.analysis.metrics;

import com.archmind.backend.analysis.graph.ArchitectureGraph;

public class ArchitectureMetrics {

    private final PackageMetrics packageMetrics = new PackageMetrics();
    private final ClassMetrics classMetrics = new ClassMetrics();

    public int getPackageCount(ArchitectureGraph graph) {
        return packageMetrics.countPackages(graph);
    }

    public int getClassCount(ArchitectureGraph graph) {
        return classMetrics.countClasses(graph);
    }

    public double averageClassesPerPackage(ArchitectureGraph graph) {

        int packages = getPackageCount(graph);

        if (packages == 0) {
            return 0;
        }

        return (double) getClassCount(graph) / packages;
    }
}