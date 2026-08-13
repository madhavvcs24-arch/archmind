package com.archmind.backend.analysis.controller;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.archmind.backend.analysis.dto.AnalysisResult;
import com.archmind.backend.analysis.dto.QualityResponse;
import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.quality.ArchitectureQualityAnalyzer;
import com.archmind.backend.analysis.quality.QualityReport;
import com.archmind.backend.analysis.service.AnalysisService;
import com.archmind.backend.analysis.service.ZipExtractorService;
import com.archmind.backend.visualisation.dto.MermaidResponse;
import com.archmind.backend.visualisation.service.MermaidService;
import com.archmind.backend.visualisation.service.PackageMermaidService;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/analysis")
public class AnalysisController {
    private final MermaidService mermaidService;
    private final PackageMermaidService packageMermaidService;
    private final AnalysisService analysisService;
    private final ZipExtractorService zipExtractorService;
    private final ArchitectureQualityAnalyzer qualityAnalyzer;
    public AnalysisController(
            AnalysisService analysisService,
            ZipExtractorService zipExtractorService,
            MermaidService mermaidService,
            PackageMermaidService packageMermaidService,
            ArchitectureQualityAnalyzer qualityAnalyzer) {
        this.analysisService = analysisService;
        this.zipExtractorService = zipExtractorService;
        this.mermaidService = mermaidService;
        this.qualityAnalyzer = qualityAnalyzer;
        this.packageMermaidService = packageMermaidService; 
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public AnalysisResult analyzeProject(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        Path projectDirectory = zipExtractorService.extract(file);

        return analysisService.analyzeProject(projectDirectory);
    }
    @PostMapping(
        value = "/diagram",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MermaidResponse generateDiagram(
        @RequestParam("file") MultipartFile file)
        throws IOException {

        Path projectDirectory = zipExtractorService.extract(file);

        ArchitectureGraph graph =
                analysisService.buildArchitectureGraph(projectDirectory);

        String diagram = mermaidService.generate(graph);

        return new MermaidResponse(diagram);
    }
    @PostMapping(
            value = "/package-diagram",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public MermaidResponse generatePackageDiagram(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        Path projectDirectory =
                zipExtractorService.extract(file);

        ArchitectureGraph graph =
                analysisService.buildArchitectureGraph(projectDirectory);

        String diagram =
                packageMermaidService.generate(graph);

        return new MermaidResponse(diagram);
    }
    @PostMapping(
        value = "/quality",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public QualityResponse analyzeQuality(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        Path projectDirectory = zipExtractorService.extract(file);

        ArchitectureGraph graph =
                analysisService.buildArchitectureGraph(projectDirectory);

        QualityReport report = qualityAnalyzer.analyze(graph);

        QualityResponse response = new QualityResponse(
        report.getArchitectureScore(),
        report.getCoupling(),
        report.getWarnings());

        System.out.println("Score = " + report.getArchitectureScore());
        System.out.println("Warnings = " + report.getWarnings());

        return response;
    }
}