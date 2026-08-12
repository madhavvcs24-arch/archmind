package com.archmind.backend.analysis.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QualityResponse {

    private Map<String, Integer> coupling;
    private int architectureScore;
    private List<String> warnings = new ArrayList<>();

    public QualityResponse(
            int architectureScore,
            Map<String, Integer> coupling,
            List<String> warnings) {

        this.architectureScore = architectureScore;
        this.coupling = coupling;
        this.warnings = warnings;
    }

    public QualityResponse(Map<String, Integer> coupling) {
        this.coupling = coupling;
    }

    public int getArchitectureScore() {
        return architectureScore;
    }

    public void setArchitectureScore(int architectureScore) {
        this.architectureScore = architectureScore;
    }

    public Map<String, Integer> getCoupling() {
        return coupling;
    }

    public void setCoupling(Map<String, Integer> coupling) {
        this.coupling = coupling;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}