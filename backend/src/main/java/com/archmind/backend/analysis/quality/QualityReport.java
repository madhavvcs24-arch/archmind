package com.archmind.backend.analysis.quality;

import java.util.Map;

public class QualityReport {

    private Map<String, Integer> coupling;
    private int architectureScore;
    private java.util.List<String> warnings = new java.util.ArrayList<>();
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

    public java.util.List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(java.util.List<String> warnings) {
        this.warnings = warnings;
    }
}