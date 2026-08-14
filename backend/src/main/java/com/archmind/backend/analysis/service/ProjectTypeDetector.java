package com.archmind.backend.analysis.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.archmind.backend.analysis.model.ProjectType;

@Service
public class ProjectTypeDetector {

    public ProjectType detect(Path projectRoot) throws IOException {

        if (Files.exists(projectRoot.resolve("pom.xml"))) {
            return ProjectType.SPRING_BOOT;
        }

        if (Files.exists(projectRoot.resolve("build.gradle"))) {
            return ProjectType.SPRING_BOOT;
        }

        if (Files.exists(projectRoot.resolve("package.json"))) {
            return ProjectType.REACT;
        }

        return ProjectType.UNKNOWN;
    }
}