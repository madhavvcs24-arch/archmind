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

        for (DependencyEdge edge : graph.getDependencies()) {

            adjacency
                    .computeIfAbsent(
                            edge.getSource(),
                            k -> new ArrayList<>()
                    )
                    .add(edge.getTarget());
        }

        Set<String> uniqueCycles = new HashSet<>();

        for (String node : adjacency.keySet()) {

            detect(
                    node,
                    node,
                    adjacency,
                    new ArrayList<>(),
                    uniqueCycles
            );
        }

        return new ArrayList<>(uniqueCycles);
    }

    private void detect(
            String start,
            String current,
            Map<String, List<String>> graph,
            List<String> path,
            Set<String> cycles) {

        path.add(current);

        List<String> neighbours =
                graph.getOrDefault(current, List.of());

        for (String next : neighbours) {

            if (next.equals(start) && path.size() > 1) {

                cycles.add(
                        normalizeCycle(path)
                );

                continue;
            }

            if (!path.contains(next)) {

                detect(
                        start,
                        next,
                        graph,
                        new ArrayList<>(path),
                        cycles
                );
            }
        }
    }

    private String normalizeCycle(List<String> path) {

        List<String> cycle =
                new ArrayList<>(path);

        int smallestIndex = 0;

        for (int i = 1; i < cycle.size(); i++) {

            if (cycle.get(i).compareTo(
                    cycle.get(smallestIndex)) < 0) {

                smallestIndex = i;
            }
        }

        List<String> normalized =
                new ArrayList<>();

        for (int i = 0; i < cycle.size(); i++) {

            normalized.add(
                    cycle.get(
                            (smallestIndex + i)
                                    % cycle.size()
                    )
            );
        }

        return String.join(
                " -> ",
                normalized
        ) + " -> " + normalized.get(0);
    }
}
