package com.archmind.backend.analysis.metrics;

import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.graph.PackageNode;

public class ClassMetrics {

    public int countClasses(ArchitectureGraph graph) {

        int total = 0;

        for (PackageNode packageNode : graph.getPackages()) {
            total += packageNode.getClasses().size();
        }

        return total;
    }
    public void test() {
    }
}