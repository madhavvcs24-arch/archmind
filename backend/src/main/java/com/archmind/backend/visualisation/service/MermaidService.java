package com.archmind.backend.visualisation.service;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.dependency.DependencyEdge;
import com.archmind.backend.analysis.graph.ArchitectureGraph;

@Service
public class MermaidService {

    public String generate(ArchitectureGraph graph) {

        StringBuilder mermaid = new StringBuilder();

        mermaid.append("graph TD\n\n");

        for (DependencyEdge edge : graph.getDependencies()) {

            mermaid.append(sanitize(edge.getSource()))
                    .append(" -->|")
                    .append(edge.getType())
                    .append("| ")
                    .append(sanitize(edge.getTarget()))
                    .append("\n");
        }

        return mermaid.toString();
    }

    private String sanitize(String value) {

        if (value == null || value.isBlank()) {
            return "Unknown";
        }

        return value
                .replace(".", "_")
                .replace("-", "_")
                .replace("$", "_");
    }
}