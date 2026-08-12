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

            String source = edge.getSource();
            String target = edge.getTarget();

            // Ignore imports from Java, Spring, Jakarta, etc.
            // Internal project classes are represented as simple class names.
            if (!isInternalClass(source) || !isInternalClass(target)) {
                continue;
            }

            mermaid.append(sanitize(source))
                    .append(" -->|")
                    .append(edge.getType())
                    .append("| ")
                    .append(sanitize(target))
                    .append("\n");
        }

        return mermaid.toString();
    }

    private boolean isInternalClass(String value) {

        if (value == null || value.isBlank()) {
            return false;
        }

        /*
         * The analyzer currently represents project classes like:
         *
         * AnalysisController
         * AnalysisService
         * ArchitectureGraph
         *
         * External imports are represented like:
         *
         * java_io_IOException
         * java_nio_file_Path
         * org_springframework_...
         */
        return value.matches("[A-Z][A-Za-z0-9$]*");
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