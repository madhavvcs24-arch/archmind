package com.archmind.backend.visualisation.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.dependency.DependencyEdge;
import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.graph.ClassNode;
import com.archmind.backend.analysis.graph.PackageNode;

@Service
public class PackageMermaidService {

    public String generate(ArchitectureGraph graph) {

        StringBuilder mermaid = new StringBuilder();

        mermaid.append("graph TD\n\n");

        // Map class name -> package name
        Map<String, String> classToPackage = new HashMap<>();

        for (PackageNode packageNode : graph.getPackages()) {

            for (ClassNode classNode : packageNode.getClasses()) {

                classToPackage.put(
                        classNode.getClassName(),
                        packageNode.getName()
                );
            }
        }

        // Convert class dependencies into package dependencies
        for (DependencyEdge edge : graph.getDependencies()) {

            String sourcePackage =
                    classToPackage.get(edge.getSource());

            String targetPackage =
                    classToPackage.get(
                            getSimpleName(edge.getTarget())
                    );

            // Ignore dependencies outside the project
            if (sourcePackage == null || targetPackage == null) {
                continue;
            }

            // Ignore dependencies inside the same package
            if (sourcePackage.equals(targetPackage)) {
                continue;
            }

            String sourceId = sanitize(sourcePackage);
            String targetId = sanitize(targetPackage);

            mermaid.append(sourceId)
                    .append("[\"")
                    .append(sourcePackage)
                    .append("\"] -->|")
                    .append(edge.getType())
                    .append("| ")
                    .append(targetId)
                    .append("[\"")
                    .append(targetPackage)
                    .append("\"]\n");
        }

        return mermaid.toString();
    }

    private String getSimpleName(String name) {

        if (name == null || name.isBlank()) {
            return "";
        }

        int lastDot = name.lastIndexOf('.');

        if (lastDot == -1) {
            return name;
        }

        return name.substring(lastDot + 1);
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