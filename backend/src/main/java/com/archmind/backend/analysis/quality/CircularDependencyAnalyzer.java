package com.archmind.backend.analysis.quality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.archmind.backend.analysis.dependency.DependencyEdge;
import com.archmind.backend.analysis.graph.ArchitectureGraph;

public class CircularDependencyAnalyzer {

    public List<String> detect(ArchitectureGraph graph) {

        Map<String, List<String>> adjacency = new HashMap<>();

        for (DependencyEdge edge : graph.getDependencies()) {
            adjacency
                    .computeIfAbsent(edge.getSource(), k -> new ArrayList<>())
                    .add(edge.getTarget());
        }

        List<String> cycles = new ArrayList<>();

        for (String node : adjacency.keySet()) {
            detect(node, node, adjacency, new ArrayList<>(), cycles);
        }

        return cycles;
    }

    private void detect(
            String start,
            String current,
            Map<String, List<String>> graph,
            List<String> path,
            List<String> cycles) {

        path.add(current);

        List<String> neighbours =
                graph.getOrDefault(current, List.of());

        for (String next : neighbours) {

            if (next.equals(start) && path.size() > 1) {
                cycles.add(String.join(" -> ", path) + " -> " + start);
            }

            if (!path.contains(next)) {
                detect(start, next, graph, new ArrayList<>(path), cycles);
            }
        }
    }
}