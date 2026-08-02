package com.archmind.backend.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.archmind.backend.common.response.ApiResponse;
import com.archmind.backend.project.dto.CreateProjectRequest;
import com.archmind.backend.project.service.ProjectService;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // CREATE PROJECT
    @PostMapping
    public ApiResponse createProject(@RequestBody CreateProjectRequest request) {

        return projectService.createProject(request);
    }

    // GET ALL PROJECTS
    @GetMapping
    public ApiResponse getAllProjects() {

        return projectService.getAllProjects();
    }
}