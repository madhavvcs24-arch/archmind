package com.archmind.backend.analysis.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ZipExtractorService {

    public Path extract(MultipartFile zipFile) throws IOException {

        Path tempDirectory = Files.createTempDirectory("archmind-");

        try (
                InputStream inputStream = zipFile.getInputStream();
                ZipInputStream zipInputStream = new ZipInputStream(inputStream)
        ) {

            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {

                System.out.println("--------------------------------");
                System.out.println("ENTRY      : " + entry.getName());
                System.out.println("DIRECTORY? : " + entry.isDirectory());

                Path outputPath = tempDirectory.resolve(entry.getName()).normalize();

                System.out.println("OUTPUT PATH: " + outputPath);

                // Zip Slip protection
                if (!outputPath.startsWith(tempDirectory)) {
                    throw new IOException("Invalid ZIP entry: " + entry.getName());
                }

                if (entry.isDirectory()) {

                    System.out.println("Creating directory...");
                    Files.createDirectories(outputPath);

                } else {

                    System.out.println("Creating parent...");
                    Files.createDirectories(outputPath.getParent());

                    System.out.println("Copying file...");
                    Files.copy(
                            zipInputStream,
                            outputPath,
                            StandardCopyOption.REPLACE_EXISTING
                    );
                }

                zipInputStream.closeEntry();
            }
        }

        return tempDirectory;
    }
}