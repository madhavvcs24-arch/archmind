package com.archmind.backend.analysis.parser;

import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.graph.ClassNode;
import com.archmind.backend.analysis.graph.PackageNode;

public class ArchitectureParser {

    private final PackageExtractor packageExtractor = new PackageExtractor();
    private final ClassExtractor classExtractor = new ClassExtractor();

    /**
     * Adds one Java source file into an existing ArchitectureGraph.
     */
    public void parse(String sourceCode, ArchitectureGraph graph) {

        String packageName = packageExtractor.extractPackage(sourceCode);
        String className = classExtractor.extractClassName(sourceCode);

        PackageNode packageNode = graph.getOrCreatePackage(packageName);

        packageNode.addClass(new ClassNode(className, packageName));
    }

    /**
     * Convenience method for parsing a single file.
     */
    public ArchitectureGraph parse(String sourceCode) {

        ArchitectureGraph graph = new ArchitectureGraph();

        parse(sourceCode, graph);

        return graph;
    }
}