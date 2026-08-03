package com.archmind.backend.project.service;

import org.springframework.stereotype.Service;

import com.archmind.backend.common.response.ApiResponse;
import com.archmind.backend.project.dto.CreateProjectRequest;
import com.archmind.backend.project.entity.Project;
import com.archmind.backend.project.repository.ProjectRepository;

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
    public ApiResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project == null) {
            return new ApiResponse(
                    false,
                    "Project not found",
                    null
            );
        }

        return new ApiResponse(
                true,
                "Project found",
                project
        );
    }
    public ApiResponse updateProject(Long id, CreateProjectRequest request) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project == null) {
            return new ApiResponse(
                    false,
                    "Project not found",
                    null
            );
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setGithubUrl(request.getGithubUrl());

        Project updatedProject = projectRepository.save(project);

        return new ApiResponse(
                true,
                "Project updated successfully",
                updatedProject
        );
    }
    public ApiResponse deleteProject(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project == null) {
            return new ApiResponse(
                    false,
                    "Project not found",
                    null
            );
        }

        projectRepository.delete(project);

        return new ApiResponse(
                true,
                "Project deleted successfully",
                null
        );
    }
}