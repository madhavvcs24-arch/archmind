package com.archmind.backend.analysis.parser;

import com.archmind.backend.analysis.detector.InheritanceDetector;
import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.graph.ClassNode;
import com.archmind.backend.analysis.graph.PackageNode;

public class ArchitectureParser {

    private final PackageExtractor packageExtractor = new PackageExtractor();
    private final ClassExtractor classExtractor = new ClassExtractor();
    private final InheritanceDetector inheritanceDetector = new InheritanceDetector();

    public ArchitectureGraph parse(String sourceCode) {

        ArchitectureGraph graph = new ArchitectureGraph();

        // Extract package name
        String packageName = packageExtractor.extractPackage(sourceCode);

        // Extract class name
        String className = classExtractor.extractClassName(sourceCode);

        // Detect parent class
        String parentClass = inheritanceDetector.detectParentClass(sourceCode);

        // Find package
        PackageNode packageNode = graph.findPackage(packageName);

        if (packageNode == null) {
            packageNode = new PackageNode(packageName);
            graph.addPackage(packageNode);
        }

        // Create class node
        ClassNode classNode = new ClassNode(className, packageName);
        classNode.setParentClass(parentClass);

        packageNode.addClass(classNode);

        return graph;
    }
}