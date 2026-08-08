package com.archmind.backend.analysis.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ZipExtractorService {

    /**
     * Extracts an uploaded ZIP file into a temporary directory.
     */
    public Path extract(MultipartFile zipFile) throws IOException {

        Path tempDirectory = Files.createTempDirectory("archmind-");

        try (InputStream inputStream = zipFile.getInputStream();
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {

            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {

                Path outputPath = tempDirectory.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(outputPath);
                } else {

                    if (outputPath.getParent() != null) {
                        Files.createDirectories(outputPath.getParent());
                    }

                    Files.copy(zipInputStream, outputPath);
                }

                zipInputStream.closeEntry();
            }
        }

        return tempDirectory;
    }
}