package com.archmind.backend.analysis.quality;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.graph.ArchitectureGraph;

@Service
public class ArchitectureQualityAnalyzer {

    private final CouplingAnalyzer couplingAnalyzer =
            new CouplingAnalyzer();

    private final CircularDependencyAnalyzer
            circularDependencyAnalyzer =
            new CircularDependencyAnalyzer();

    public QualityReport analyze(ArchitectureGraph graph) {

        QualityReport report = new QualityReport();

        // -----------------------------------------
        // 1. Calculate coupling
        // -----------------------------------------

        Map<String, Integer> coupling =
                couplingAnalyzer.calculate(graph);

        report.setCoupling(coupling);

        // -----------------------------------------
        // 2. Detect circular dependencies
        // -----------------------------------------

        List<String> cycles =
                circularDependencyAnalyzer.detect(graph);

        // -----------------------------------------
        // 3. Generate warnings
        // -----------------------------------------

        List<String> warnings =
                new ArrayList<>();

        for (Map.Entry<String, Integer> entry :
                coupling.entrySet()) {

            String className = entry.getKey();
            int dependencyCount = entry.getValue();

            if (dependencyCount > 10) {

                warnings.add(
                        "Very high coupling: "
                                + className
                                + " has "
                                + dependencyCount
                                + " dependencies."
                );

            } else if (dependencyCount > 5) {

                warnings.add(
                        "High coupling: "
                                + className
                                + " has "
                                + dependencyCount
                                + " dependencies."
                );
            }
        }

        for (String cycle : cycles) {

            warnings.add(
                    "Circular dependency detected: "
                            + cycle
            );
        }

        report.setWarnings(warnings);

        // -----------------------------------------
        // 4. Calculate architecture score
        // -----------------------------------------

        int score = 100;

        // Coupling penalties
        for (int dependencyCount :
                coupling.values()) {

            if (dependencyCount > 10) {

                score -= 10;

            } else if (dependencyCount > 5) {

                score -= 5;
            }
        }

        // Circular dependency penalties
        score -= cycles.size() * 10;

        // Never allow score below 0
        score = Math.max(score, 0);

        report.setArchitectureScore(score);

        return report;
    }
}