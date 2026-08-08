package com.archmind.backend.analysis.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.archmind.backend.analysis.dto.AnalysisResult;
import com.archmind.backend.analysis.service.AnalysisService;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping
    public AnalysisResult analyze(@RequestBody String sourceCode) {
        return analysisService.analyze(sourceCode);
    }
}