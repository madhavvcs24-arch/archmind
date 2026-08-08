package com.archmind.backend.analysis.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ProjectScannerService {

    /**
     * Returns all Java source files inside a project directory.
     */
    public List<Path> findJavaFiles(Path projectRoot) throws IOException {

        return Files.walk(projectRoot)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .collect(Collectors.toList());
    }
}