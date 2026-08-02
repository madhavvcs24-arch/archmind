package com.archmind.backend.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.archmind.backend.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}