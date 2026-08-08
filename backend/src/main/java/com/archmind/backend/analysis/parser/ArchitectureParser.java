package com.archmind.backend.analysis.parser;

import java.util.List;

import com.archmind.backend.analysis.detector.ImportDetector;
import com.archmind.backend.analysis.detector.InheritanceDetector;
import com.archmind.backend.analysis.detector.InterfaceDetector;
import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.graph.ClassNode;
import com.archmind.backend.analysis.graph.PackageNode;

public class ArchitectureParser {

    private final PackageExtractor packageExtractor = new PackageExtractor();
    private final ClassExtractor classExtractor = new ClassExtractor();
    private final InheritanceDetector inheritanceDetector = new InheritanceDetector();
    private final InterfaceDetector interfaceDetector = new InterfaceDetector();
    private final ImportDetector importDetector = new ImportDetector();

    public ArchitectureGraph parse(String sourceCode) {

        ArchitectureGraph graph = new ArchitectureGraph();

        // Extract package
        String packageName = packageExtractor.extractPackage(sourceCode);

        // Extract class
        String className = classExtractor.extractClassName(sourceCode);

        // Detect inheritance
        String parentClass = inheritanceDetector.detectParentClass(sourceCode);

        // Detect interfaces
        List<String> interfaces = interfaceDetector.detectInterfaces(sourceCode);

        // Detect imports
        List<String> imports = importDetector.detectImports(sourceCode);

        // Find package
        PackageNode packageNode = graph.findPackage(packageName);

        if (packageNode == null) {
            packageNode = new PackageNode(packageName);
            graph.addPackage(packageNode);
        }

        // Create class node
        ClassNode classNode = new ClassNode(className, packageName);
        classNode.setParentClass(parentClass);

        // Store interfaces
        for (String interfaceName : interfaces) {
            classNode.addImplementedInterface(interfaceName);
        }

        // Store imports
        for (String importName : imports) {
            classNode.addImport(importName);
        }

        packageNode.addClass(classNode);

        return graph;
    }
}