package com.archmind.backend.analysis.quality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.archmind.backend.analysis.dependency.DependencyEdge;
import com.archmind.backend.analysis.graph.ArchitectureGraph;

public class CircularDependencyAnalyzer {

    public List<String> detect(ArchitectureGraph graph) {

        Map<String, List<String>> adjacency = new HashMap<>();

        // Build adjacency list
        for (DependencyEdge edge : graph.getDependencies()) {

            adjacency
                    .computeIfAbsent(
                            edge.getSource(),
                            k -> new ArrayList<>()
                    )
                    .add(edge.getTarget());
        }

        Set<String> uniqueCycles = new HashSet<>();

        // Start DFS from every node
        for (String node : adjacency.keySet()) {

            detectCycle(
                    node,
                    node,
                    adjacency,
                    new ArrayList<>(),
                    new HashSet<>(),
                    uniqueCycles
            );
        }

        return new ArrayList<>(uniqueCycles);
    }

    private void detectCycle(
            String start,
            String current,
            Map<String, List<String>> graph,
            List<String> path,
            Set<String> visited,
            Set<String> cycles) {

        path.add(current);
        visited.add(current);

        List<String> neighbours =
                graph.getOrDefault(current, List.of());

        for (String next : neighbours) {

            // We returned to the starting node
            if (next.equals(start) && path.size() > 1) {

                cycles.add(
                        normalizeCycle(path)
                );

                continue;
            }

            // Continue DFS only if node is not already
            // present in the current path
            if (!visited.contains(next)) {

                detectCycle(
                        start,
                        next,
                        graph,
                        new ArrayList<>(path),
                        new HashSet<>(visited),
                        cycles
                );
            }
        }
    }

    private String normalizeCycle(List<String> path) {

        /*
         * Example:
         *
         * A -> B -> C
         *
         * B -> C -> A
         *
         * C -> A -> B
         *
         * All become:
         *
         * A -> B -> C -> A
         */

        int smallestIndex = 0;

        for (int i = 1; i < path.size(); i++) {

            if (path.get(i).compareTo(
                    path.get(smallestIndex)) < 0) {

                smallestIndex = i;
            }
        }

        List<String> normalized =
                new ArrayList<>();

        for (int i = 0; i < path.size(); i++) {

            normalized.add(
                    path.get(
                            (smallestIndex + i)
                                    % path.size()
                    )
            );
        }

        return String.join(
                " -> ",
                normalized
        ) + " -> " + normalized.get(0);
    }
}   