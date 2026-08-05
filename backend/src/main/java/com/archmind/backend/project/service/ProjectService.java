package com.archmind.backend.project.service;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.archmind.backend.common.response.ApiResponse;
import com.archmind.backend.project.dto.CreateProjectRequest;
import com.archmind.backend.project.dto.ProjectResponse;
import com.archmind.backend.project.entity.Project;
import com.archmind.backend.project.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    public ProjectService(ProjectRepository projectRepository,
                          ModelMapper modelMapper) {

        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
    }

    // CREATE PROJECT
    public ApiResponse createProject(CreateProjectRequest request) {

        Project project = new Project();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setGithubUrl(request.getGithubUrl());

        Project savedProject = projectRepository.save(project);

        ProjectResponse response =
                modelMapper.map(savedProject, ProjectResponse.class);

        return new ApiResponse(
                true,
                "Project created successfully",
                response
        );
    }

    // GET ALL PROJECTS (SEARCH + SORT + PAGINATION)
        public ApiResponse getAllProjects(
                String keyword,
                int page,
                int size,
                String sortBy,
                String sortDir
        ) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Project> projectPage;

        if (keyword == null || keyword.isBlank()) {
                projectPage = projectRepository.findAll(pageable);
        } else {
                projectPage = projectRepository.findByNameContainingIgnoreCase(
                        keyword,
                        pageable
                );
        }

        List<ProjectResponse> projects = projectPage
                .getContent()
                .stream()
                .map(project -> modelMapper.map(project, ProjectResponse.class))
                .toList();

        return new ApiResponse(
                true,
                "Projects fetched successfully",
                projects
        );
        }

    // GET PROJECT BY ID
    public ApiResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id).orElse(null);

        if (project == null) {
            return new ApiResponse(
                    false,
                    "Project not found",
                    null
            );
        }

        ProjectResponse response =
                modelMapper.map(project, ProjectResponse.class);

        return new ApiResponse(
                true,
                "Project found",
                response
        );
    }

    // UPDATE PROJECT
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

        ProjectResponse response =
                modelMapper.map(updatedProject, ProjectResponse.class);

        return new ApiResponse(
                true,
                "Project updated successfully",
                response
        );
    }

    // DELETE PROJECT
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