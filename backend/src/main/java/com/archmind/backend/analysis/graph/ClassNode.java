package com.archmind.backend.analysis.graph;

import java.util.ArrayList;
import java.util.List;

public class ClassNode {

    private String className;
    private String packageName;
    private String parentClass;
    private boolean isInterface;

    private final List<String> implementedInterfaces = new ArrayList<>();
    private final List<String> imports = new ArrayList<>();

    public ClassNode() {
    }

    public ClassNode(String className, String packageName) {
        this.className = className;
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public List<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public void addImplementedInterface(String interfaceName) {
        implementedInterfaces.add(interfaceName);
    }

    public List<String> getImports() {
        return imports;
    }

    public void addImport(String importName) {
        imports.add(importName);
    }
    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean isInterface) {
        this.isInterface = isInterface;
    }
}