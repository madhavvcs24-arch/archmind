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

    public ClassNode parse(String sourceCode, ArchitectureGraph graph) {

        String packageName = packageExtractor.extractPackage(sourceCode);
        String className = classExtractor.extractClassName(sourceCode);

        String parentClass =
                inheritanceDetector.detectParentClass(sourceCode);

        List<String> interfaces =
                interfaceDetector.detectInterfaces(sourceCode);

        List<String> imports =
                importDetector.detectImports(sourceCode);

        PackageNode packageNode = graph.findPackage(packageName);

        if (packageNode == null) {
            packageNode = new PackageNode(packageName);
            graph.addPackage(packageNode);
        }

        // Create ONE ClassNode
        ClassNode classNode =
                new ClassNode(className, packageName);

        classNode.setParentClass(parentClass);

        classNode.setInterface(
                interfaceDetector.isInterface(sourceCode)
        );

        for (String interfaceName : interfaces) {
            classNode.addImplementedInterface(interfaceName);
        }

        for (String importName : imports) {
            classNode.addImport(importName);
        }

        // Add the SAME object to the graph
        packageNode.addClass(classNode);

        return classNode;
    }
}