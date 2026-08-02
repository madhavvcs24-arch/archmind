package com.archmind.backend.project.service;

import com.archmind.backend.common.response.ApiResponse;
import com.archmind.backend.project.dto.CreateProjectRequest;
import com.archmind.backend.project.entity.Project;
import com.archmind.backend.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // CREATE PROJECT
    public ApiResponse createProject(CreateProjectRequest request) {

        Project project = new Project();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setGithubUrl(request.getGithubUrl());

        Project savedProject = projectRepository.save(project);

        return new ApiResponse(
                true,
                "Project created successfully",
                savedProject
        );
    }

    // GET ALL PROJECTS
    public ApiResponse getAllProjects() {

        return new ApiResponse(
                true,
                "Projects fetched successfully",
                projectRepository.findAll()
        );
    }
}