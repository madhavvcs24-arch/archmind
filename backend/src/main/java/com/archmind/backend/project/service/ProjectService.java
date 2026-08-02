package com.archmind.backend.project.service;

import org.springframework.stereotype.Service;

import com.archmind.backend.common.response.ApiResponse;
import com.archmind.backend.project.dto.CreateProjectRequest;

@Service
public class ProjectService {

    public ApiResponse createProject(CreateProjectRequest request) {

        return new ApiResponse(
                true,
                "Project created successfully",
                request
        );
    }
}