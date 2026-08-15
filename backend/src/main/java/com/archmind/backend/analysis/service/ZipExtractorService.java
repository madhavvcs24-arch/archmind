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

        Path tempDirectory =
                Files.createTempDirectory("archmind-");

        try (
                InputStream inputStream = zipFile.getInputStream();
                ZipInputStream zipInputStream =
                        new ZipInputStream(inputStream)
        ) {

            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {

                String originalEntryName = entry.getName();

                if (originalEntryName == null
                        || originalEntryName.isBlank()) {

                    zipInputStream.closeEntry();
                    continue;
                }

                /*
                 * ZIP files may use Windows-style '\' separators.
                 * Convert them to '/' before creating filesystem paths.
                 */
                String entryName =
                        originalEntryName.replace('\\', '/');

                /*
                 * Some ZIP creators incorrectly mark directory
                 * entries as files.
                 *
                 * Therefore we check the name as well.
                 */
                boolean isDirectory =
                        entry.isDirectory()
                                || originalEntryName.endsWith("/")
                                || originalEntryName.endsWith("\\");

                System.out.println("--------------------------------");
                System.out.println(
                        "ENTRY          : " + originalEntryName
                );
                System.out.println(
                        "NORMALIZED     : " + entryName
                );
                System.out.println(
                        "ZIP DIRECTORY? : " + entry.isDirectory()
                );
                System.out.println(
                        "IS DIRECTORY?  : " + isDirectory
                );

                Path outputPath =
                        tempDirectory
                                .resolve(entryName)
                                .normalize();

                System.out.println(
                        "OUTPUT PATH    : " + outputPath
                );

                /*
                 * ZIP SLIP PROTECTION
                 */
                if (!outputPath.startsWith(tempDirectory)) {

                    throw new IOException(
                            "Invalid ZIP entry: " + originalEntryName
                    );
                }

                /*
                 * DIRECTORY
                 */
                if (isDirectory) {

                    System.out.println(
                            "Creating directory..."
                    );

                    /*
                     * If something already exists at this path,
                     * make sure it is actually a directory.
                     */
                    if (Files.exists(outputPath)) {

                        if (!Files.isDirectory(outputPath)) {

                            throw new IOException(
                                    "ZIP entry conflicts with existing file: "
                                            + originalEntryName
                            );
                        }

                    } else {

                        Files.createDirectories(outputPath);
                    }
                }

                /*
                 * FILE
                 */
                else {

                    System.out.println(
                            "Creating parent..."
                    );

                    Path parent = outputPath.getParent();

                    if (parent != null) {

                        Files.createDirectories(parent);
                    }

                    /*
                     * A file cannot replace an existing directory.
                     */
                    if (Files.exists(outputPath)
                            && Files.isDirectory(outputPath)) {

                        throw new IOException(
                                "ZIP entry conflicts with existing directory: "
                                        + originalEntryName
                        );
                    }

                    System.out.println(
                            "Copying file..."
                    );

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