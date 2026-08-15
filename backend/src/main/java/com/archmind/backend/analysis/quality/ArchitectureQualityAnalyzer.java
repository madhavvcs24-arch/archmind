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

            int dependencyCount =
                    entry.getValue();

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
        // 4. Generate recommendations
        // -----------------------------------------

        List<String> recommendations =
                new ArrayList<>();

        for (Map.Entry<String, Integer> entry :
                coupling.entrySet()) {

            String className = entry.getKey();

            int dependencyCount =
                    entry.getValue();

            if (dependencyCount > 10) {

                recommendations.add(
                        "Consider splitting "
                                + className
                                + " into smaller services "
                                + "with more focused responsibilities."
                );

            } else if (dependencyCount > 5) {

                recommendations.add(
                        "Consider reducing the dependencies "
                                + "of "
                                + className
                                + " by extracting some "
                                + "responsibilities into separate components."
                );
            }
        }

        for (String cycle : cycles) {

            recommendations.add(
                    "Break the circular dependency "
                            + cycle
                            + " by introducing an abstraction "
                            + "or restructuring the dependencies."
            );
        }

        // If there are no problems, provide a positive recommendation.

        if (recommendations.isEmpty()) {

            recommendations.add(
                    "Architecture looks healthy. "
                            + "Continue maintaining low coupling "
                            + "and clear separation of responsibilities."
            );
        }

        report.setRecommendations(
                recommendations
        );

        // -----------------------------------------
        // 5. Calculate architecture score
        // -----------------------------------------

        int score = 100;

        List<String> scoreBreakdown =
                new ArrayList<>();

        scoreBreakdown.add(
                "Base architecture score: +100"
        );

        // -----------------------------------------
        // Coupling penalties
        // -----------------------------------------

        for (Map.Entry<String, Integer> entry :
                coupling.entrySet()) {

            String className = entry.getKey();

            int dependencyCount =
                    entry.getValue();

            if (dependencyCount > 10) {

                score -= 10;

                scoreBreakdown.add(
                        "Very high coupling in "
                                + className
                                + ": -10"
                );

            } else if (dependencyCount > 5) {

                score -= 5;

                scoreBreakdown.add(
                        "High coupling in "
                                + className
                                + ": -5"
                );
            }
        }

        // -----------------------------------------
        // Circular dependency penalties
        // -----------------------------------------

        if (!cycles.isEmpty()) {

            int penalty =
                    cycles.size() * 10;

            score -= penalty;

            scoreBreakdown.add(
                    cycles.size()
                            + " circular dependency cycle(s): -"
                            + penalty
            );
        }

        // -----------------------------------------
        // Overall coupling penalty
        // -----------------------------------------

        if (!coupling.isEmpty()) {

            double totalDependencies = 0;

            for (int dependencyCount :
                    coupling.values()) {

                totalDependencies += dependencyCount;
            }

            double averageCoupling =
                    totalDependencies / coupling.size();

            if (averageCoupling > 5) {

                score -= 10;

                scoreBreakdown.add(
                        String.format(
                                "High average coupling (%.2f): -10",
                                averageCoupling
                        )
                );

            } else if (averageCoupling > 3) {

                score -= 5;

                scoreBreakdown.add(
                        String.format(
                                "Elevated average coupling (%.2f): -5",
                                averageCoupling
                        )
                );

            } else {

                scoreBreakdown.add(
                        String.format(
                                "Healthy average coupling (%.2f): no penalty",
                                averageCoupling
                        )
                );
            }
        }

        // -----------------------------------------
        // Keep score between 0 and 100
        // -----------------------------------------

        score =
                Math.max(
                        0,
                        Math.min(score, 100)
                );

        scoreBreakdown.add(
                "Final architecture score: "
                        + score
                        + "/100"
        );

        report.setScoreBreakdown(
                scoreBreakdown
        );

        report.setArchitectureScore(score);
        return report;
    }
}