package com.archmind.backend.analysis.dependency;

import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.graph.ClassNode;

public class DependencyBuilder {

    public void buildDependencies(ArchitectureGraph graph, ClassNode classNode) {

        // extends relationship
        if (classNode.getParentClass() != null) {

            graph.addDependency(
                    new DependencyEdge(
                            classNode.getClassName(),
                            classNode.getParentClass(),
                            "EXTENDS"
                    )
            );
        }

        // implements relationships
        for (String interfaceName : classNode.getImplementedInterfaces()) {

            graph.addDependency(
                    new DependencyEdge(
                            classNode.getClassName(),
                            interfaceName,
                            "IMPLEMENTS"
                    )
            );
        }

        // import relationships
        for (String importName : classNode.getImports()) {

            graph.addDependency(
                    new DependencyEdge(
                            classNode.getClassName(),
                            importName,
                            "IMPORT"
                    )
            );
        }
    }
}