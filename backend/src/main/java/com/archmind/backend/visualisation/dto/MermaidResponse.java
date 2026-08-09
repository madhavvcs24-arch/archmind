package com.archmind.backend.visualisation.dto;

public class MermaidResponse {

    private String diagram;

    public MermaidResponse() {
    }

    public MermaidResponse(String diagram) {
        this.diagram = diagram;
    }

    public String getDiagram() {
        return diagram;
    }

    public void setDiagram(String diagram) {
        this.diagram = diagram;
    }
}
