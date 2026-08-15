package com.archmind.backend.analysis.quality;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QualityReport {

    private Map<String, Integer> coupling;
    private List<String> scoreBreakdown = new ArrayList<>();
    private int architectureScore;

    private List<String> warnings = new ArrayList<>();

    private List<String> recommendations = new ArrayList<>();

    public QualityReport() {
    }

    public QualityReport(Map<String, Integer> coupling) {
        this.coupling = coupling;
    }

    public Map<String, Integer> getCoupling() {
        return coupling;
    }

    public void setCoupling(Map<String, Integer> coupling) {
        this.coupling = coupling;
    }

    public int getArchitectureScore() {
        return architectureScore;
    }

    public void setArchitectureScore(int architectureScore) {
        this.architectureScore = architectureScore;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(
            List<String> recommendations) {

        this.recommendations = recommendations;
    }
    public List<String> getScoreBreakdown() {
        return scoreBreakdown;
    }

    public void setScoreBreakdown(
            List<String> scoreBreakdown) {

        this.scoreBreakdown = scoreBreakdown;
    }
}