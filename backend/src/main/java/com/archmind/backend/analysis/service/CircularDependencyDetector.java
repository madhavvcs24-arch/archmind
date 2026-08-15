package com.archmind.backend.analysis.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.dependency.DependencyEdge;
import com.archmind.backend.analysis.graph.ArchitectureGraph;
import com.archmind.backend.analysis.model.CircularDependency;

@Service
public class CircularDependencyDetector {

    public List<CircularDependency> detect(ArchitectureGraph graph) {

        List<CircularDependency> cycles = new ArrayList<>();

        for (DependencyEdge edge : graph.getDependencies()) {

            if (edge.getSource().equals(edge.getTarget())) {
                continue;
            }

            List<String> path = new ArrayList<>();
            dfs(
                    edge.getTarget(),
                    edge.getSource(),
                    graph,
                    new HashSet<>(),
                    path,
                    cycles
            );
        }

        return cycles;
    }

    private void dfs(
            String current,
            String target,
            ArchitectureGraph graph,
            Set<String> visited,
            List<String> path,
            List<CircularDependency> cycles) {

        visited.add(current);
        path.add(current);

        if (current.equals(target)) {
            cycles.add(new CircularDependency(new ArrayList<>(path)));
            return;
        }

        for (DependencyEdge edge : graph.getDependencies()) {

            if (!edge.getSource().equals(current)) {
                continue;
            }

            if (!visited.contains(edge.getTarget())) {
                dfs(
                        edge.getTarget(),
                        target,
                        graph,
                        visited,
                        new ArrayList<>(path),
                        cycles
                );
            }
        }
    }
}