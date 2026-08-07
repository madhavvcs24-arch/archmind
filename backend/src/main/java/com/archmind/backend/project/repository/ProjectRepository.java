package com.archmind.backend.project.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.archmind.backend.project.entity.Project;
import com.archmind.backend.user.entity.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByNameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );
    List<Project> findByOwner(User owner);
}