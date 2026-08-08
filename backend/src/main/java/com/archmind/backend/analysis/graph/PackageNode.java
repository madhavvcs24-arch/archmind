package com.archmind.backend.analysis.graph;

import java.util.ArrayList;
import java.util.List;

public class PackageNode {

    private String name;
    private final List<ClassNode> classes = new ArrayList<>();

    public PackageNode() {
    }

    public PackageNode(String name) {
        this.name = name;
    }

    public void addClass(ClassNode classNode) {
        classes.add(classNode);
    }

    public String getName() {
        return name;
    }

    public List<ClassNode> getClasses() {
        return classes;
    }

    public void setName(String name) {
        this.name = name;
    }
}