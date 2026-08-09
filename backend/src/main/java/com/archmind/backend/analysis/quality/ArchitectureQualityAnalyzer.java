package com.archmind.backend.analysis.quality;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.graph.ArchitectureGraph;

@Service
public class ArchitectureQualityAnalyzer {

    private final CouplingAnalyzer couplingAnalyzer = new CouplingAnalyzer();

    public QualityReport analyze(ArchitectureGraph graph) {

        QualityReport report = new QualityReport();

        report.setCoupling(
                couplingAnalyzer.calculate(graph)
        );

        report.setArchitectureScore(100);

        return report;
    }
}