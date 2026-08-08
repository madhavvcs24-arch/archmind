package com.archmind.backend.analysis.dto;

public class AnalysisResult {

    private int packageCount;
    private int classCount;
    private double averageClassesPerPackage;

    public AnalysisResult() {
    }

    public AnalysisResult(int packageCount,
                          int classCount,
                          double averageClassesPerPackage) {
        this.packageCount = packageCount;
        this.classCount = classCount;
        this.averageClassesPerPackage = averageClassesPerPackage;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public double getAverageClassesPerPackage() {
        return averageClassesPerPackage;
    }

    public void setAverageClassesPerPackage(double averageClassesPerPackage) {
        this.averageClassesPerPackage = averageClassesPerPackage;
    }
}