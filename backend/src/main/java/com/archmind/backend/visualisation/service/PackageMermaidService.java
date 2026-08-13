package com.archmind.backend.visualisation.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

            String packageName = packageNode.getName();

            for (ClassNode classNode : packageNode.getClasses()) {

                classToPackage.put(
                        classNode.getClassName(),
                        packageName
                );
            }
        }

        // Prevent duplicate package relationships
        Set<String> packageDependencies = new HashSet<>();

        for (DependencyEdge edge : graph.getDependencies()) {

            String sourcePackage =
                    classToPackage.get(edge.getSource());

            String targetPackage =
                    classToPackage.get(getSimpleName(edge.getTarget()));

            // Ignore dependencies where we cannot identify
            // both project packages.
            if (sourcePackage == null || targetPackage == null) {
                continue;
            }

            // Ignore dependencies within the same package
            if (sourcePackage.equals(targetPackage)) {
                continue;
            }

            String source = sanitize(sourcePackage);
            String target = sanitize(targetPackage);

            String relationship =
                    source + " --> " + target;

            // Avoid duplicate arrows
            if (packageDependencies.add(relationship)) {

                mermaid.append(source)
                        .append(" --> ")
                        .append(target)
                        .append("\n");
            }
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