package com.archmind.backend.analysis.dependency;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.graph.ClassNode;

public class DependencyBuilder {

    public void buildDependencies(
            ArchitectureGraph graph,
            ClassNode classNode,
            List<ClassNode> projectClasses) {

        Set<String> projectClassNames = new HashSet<>();

        for (ClassNode projectClass : projectClasses) {
            projectClassNames.add(projectClass.getClassName());
        }

        // EXTENDS
        if (classNode.getParentClass() != null
                && isProjectClass(
                        classNode.getParentClass(),
                        projectClassNames)) {

            graph.addDependency(
                    new DependencyEdge(
                            classNode.getClassName(),
                            classNode.getParentClass(),
                            "EXTENDS"
                    )
            );
        }

        // IMPLEMENTS
        for (String interfaceName :
                classNode.getImplementedInterfaces()) {

            if (isProjectClass(
                    interfaceName,
                    projectClassNames)) {

                graph.addDependency(
                        new DependencyEdge(
                                classNode.getClassName(),
                                interfaceName,
                                "IMPLEMENTS"
                        )
                );
            }
        }

        // IMPORTS
        for (String importName :
                classNode.getImports()) {

            if (isProjectClass(
                    importName,
                    projectClassNames)) {

                String targetClass = getSimpleName(importName);

                graph.addDependency(
                        new DependencyEdge(
                                classNode.getClassName(),
                                targetClass,
                                "IMPORT"
                        )
                );
            }
        }
    }

    private boolean isProjectClass(
            String dependency,
            Set<String> projectClassNames) {

        if (dependency == null || dependency.isBlank()) {
            return false;
        }

        String simpleName = getSimpleName(dependency);

        return projectClassNames.contains(simpleName);
    }

    private String getSimpleName(String name) {

        int lastDot = name.lastIndexOf('.');

        if (lastDot == -1) {
            return name;
        }

        return name.substring(lastDot + 1);
    }
}