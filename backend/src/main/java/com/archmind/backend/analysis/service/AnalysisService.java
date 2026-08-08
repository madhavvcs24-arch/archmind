package com.archmind.backend.analysis.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.dto.AnalysisResult;
import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.metrics.ArchitectureMetrics;
import com.archmind.backend.analysis.parser.ArchitectureParser;

@Service
public class AnalysisService {

    private final ArchitectureParser parser = new ArchitectureParser();
    private final ArchitectureMetrics metrics = new ArchitectureMetrics();

    private final ProjectScannerService scannerService;
    private final FileReaderService fileReaderService;

    public AnalysisService(ProjectScannerService scannerService,
                           FileReaderService fileReaderService) {
        this.scannerService = scannerService;
        this.fileReaderService = fileReaderService;
    }

    // Existing method (keep it)
    public AnalysisResult analyze(String sourceCode) {

        ArchitectureGraph graph = parser.parse(sourceCode);

        return new AnalysisResult(
                metrics.getPackageCount(graph),
                metrics.getClassCount(graph),
                metrics.averageClassesPerPackage(graph)
        );
    }

    // NEW METHOD
    public AnalysisResult analyzeProject(Path projectRoot) throws IOException {

        List<Path> javaFiles = scannerService.findJavaFiles(projectRoot);

        ArchitectureGraph graph = new ArchitectureGraph();

        for (Path file : javaFiles) {

            String source = fileReaderService.readFile(file);

            ArchitectureGraph fileGraph = parser.parse(source);

            fileGraph.getPackages().forEach(graph::addPackage);
        }

        return new AnalysisResult(
                metrics.getPackageCount(graph),
                metrics.getClassCount(graph),
                metrics.averageClassesPerPackage(graph)
        );
    }
}