package com.archmind.backend.project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.archmind.backend.common.response.ApiResponse;
import com.archmind.backend.project.dto.CreateProjectRequest;
import com.archmind.backend.project.service.ProjectService;
import com.archmind.backend.user.entity.User;
import com.archmind.backend.user.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final UserRepository userRepository;
    private final ProjectService projectService;

    public ProjectController(
        ProjectService projectService,
        UserRepository userRepository
) {
    this.projectService = projectService;
    this.userRepository = userRepository;
}

    // CREATE PROJECT
    @PostMapping
    public ApiResponse createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        User currentUser = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found"));

        return projectService.createProject(request, currentUser);
    }

    // GET ALL PROJECTS
    @GetMapping
    public ApiResponse getAllProjects(

            @RequestParam(defaultValue = "") String keyword,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "desc") String sortDir

    ) {

        return projectService.getAllProjects(
                keyword,
                page,
                size,
                sortBy,
                sortDir
        );
    }
    @GetMapping("/{id}")
    public ApiResponse getProjectById(@PathVariable Long id) {

        return projectService.getProjectById(id);

    }
    @PutMapping("/{id}")
    public ApiResponse updateProject(
            @PathVariable Long id,
            @Valid @RequestBody CreateProjectRequest request) {

                return projectService.updateProject(id, request);

    }
    @DeleteMapping("/{id}")
    public ApiResponse deleteProject(@PathVariable Long id) {

        return projectService.deleteProject(id);

    }
}