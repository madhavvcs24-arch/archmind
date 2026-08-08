package com.archmind.backend.analysis.graph;

import java.util.ArrayList;
import java.util.List;

import com.archmind.backend.analysis.dependency.DependencyEdge;

public class ArchitectureGraph {

    private final List<PackageNode> packages = new ArrayList<>();
    private final List<DependencyEdge> dependencies = new ArrayList<>();

    public ArchitectureGraph() {
    }

    public void addPackage(PackageNode packageNode) {
        packages.add(packageNode);
    }

    public List<PackageNode> getPackages() {
        return packages;
    }

    public int getPackageCount() {
        return packages.size();
    }

    public PackageNode findPackage(String packageName) {

        for (PackageNode packageNode : packages) {
            if (packageNode.getName().equals(packageName)) {
                return packageNode;
            }
        }

        return null;
    }

    public void addDependency(DependencyEdge edge) {
        dependencies.add(edge);
    }

    public List<DependencyEdge> getDependencies() {
        return dependencies;
    }
}