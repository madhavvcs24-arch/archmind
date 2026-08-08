package com.archmind.backend.analysis.dto;

import java.util.List;

public class DependencyGraphResponse {

    private List<DependencyResponse> dependencies;

    public DependencyGraphResponse() {
    }

    public DependencyGraphResponse(List<DependencyResponse> dependencies) {
        this.dependencies = dependencies;
    }

    public List<DependencyResponse> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DependencyResponse> dependencies) {
        this.dependencies = dependencies;
    }
}