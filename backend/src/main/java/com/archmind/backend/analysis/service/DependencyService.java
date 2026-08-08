package com.archmind.backend.analysis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.dependency.DependencyEdge;
import com.archmind.backend.analysis.dto.DependencyGraphResponse;
import com.archmind.backend.analysis.dto.DependencyResponse;
import com.archmind.backend.analysis.graph.ArchitectureGraph;

@Service
public class DependencyService {

    public DependencyGraphResponse buildResponse(ArchitectureGraph graph) {

        List<DependencyResponse> responses = new ArrayList<>();

        for (DependencyEdge edge : graph.getDependencies()) {

            responses.add(
                    new DependencyResponse(
                            edge.getSource(),
                            edge.getTarget(),
                            edge.getType()
                    )
            );
        }

        return new DependencyGraphResponse(responses);
    }
}