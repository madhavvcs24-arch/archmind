package com.archmind.backend.analysis.graph;

import java.util.ArrayList;
import java.util.List;

public class ArchitectureGraph {

    private final List<PackageNode> packages = new ArrayList<>();

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
}