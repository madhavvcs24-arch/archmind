package com.archmind.backend.analysis.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.dependency.DependencyEdge;
import com.archmind.backend.analysis.dto.AnalysisResult;
import com.archmind.backend.analysis.dto.DependencyGraphResponse;
import com.archmind.backend.analysis.dto.DependencyResponse;
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

    public AnalysisResult analyzeProject(Path projectRoot) throws IOException {

        List<Path> javaFiles = scannerService.findJavaFiles(projectRoot);

        ArchitectureGraph graph = buildArchitectureGraph(projectRoot);

        return new AnalysisResult(
                metrics.getPackageCount(graph),
                metrics.getClassCount(graph),
                metrics.averageClassesPerPackage(graph)
        );
    }
    public DependencyGraphResponse getDependencyGraph(ArchitectureGraph graph) {

        ArrayList<DependencyResponse> responses = new ArrayList<>();

        for (DependencyEdge edge : graph.getDependencies()) {

            responses.add(
                    new DependencyResponse(
                            edge.getSource(),
                            edge.getTarget(),
                            edge.getType()
                    )
            );
        }

        return new DependencyGraphResponse(responses);
    }
    public ArchitectureGraph buildArchitectureGraph(Path projectRoot) throws IOException {

        List<Path> javaFiles = scannerService.findJavaFiles(projectRoot);

        ArchitectureGraph graph = new ArchitectureGraph();

        for (Path file : javaFiles) {

            String source = fileReaderService.readFile(file);

            parser.parse(source, graph);
        }

        return graph;
    }
}