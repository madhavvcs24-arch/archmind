package com.archmind.backend.analysis.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

@Service
public class FileReaderService {

    /**
     * Reads the contents of a Java source file.
     */
    public String readFile(Path file) throws IOException {

        return Files.readString(file);
    }
}