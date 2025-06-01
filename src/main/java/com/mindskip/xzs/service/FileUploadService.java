package com.mindskip.xzs.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    private final Path fileStorageLocation;
    private final String resourceServePath; 

    public FileUploadService(@Value("${file.upload-dir}") String uploadDir,
                             @Value("${file.resource-handler}") String resourceHandler) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.resourceServePath = resourceHandler.replace("/**", "/"); 
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, Long courseId) throws IOException {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            // No extension, or other error
        }
        String storedFileName = UUID.randomUUID().toString() + fileExtension;

        Path courseSpecificDir = this.fileStorageLocation.resolve(String.valueOf(courseId));
        Files.createDirectories(courseSpecificDir);

        Path targetLocation = courseSpecificDir.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        // 直接返回简化的URL路径，不包含上传目录名称
        return resourceServePath + courseId + "/" + storedFileName;
    }

    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isEmpty() || !fileUrl.startsWith(resourceServePath)) {
            throw new IllegalArgumentException("Invalid file URL to delete: " + fileUrl);
        }

        // Extract the relative path from the URL
        // e.g., if resourceServePath = "/resources/", fileUrl = "/resources/1/xyz.pdf"
        // then relativePathInConfig = "1/xyz.pdf"
        String relativePathInConfig = fileUrl.substring(resourceServePath.length());

        Path filePath = this.fileStorageLocation.resolve(relativePathInConfig).normalize();

        // Security check to prevent path traversal
        if (!filePath.startsWith(this.fileStorageLocation)) {
            throw new SecurityException("Cannot delete file outside of storage directory: " + fileUrl);
        }

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            // Log or throw an exception if the file doesn't exist, depending on requirements
            // For now, let it pass silently if file not found, as DB record will be deleted anyway
            System.out.println("File not found for deletion, but proceeding: " + filePath);
        }
    }

    // Optional: Method to load a file, if needed for direct serving or other purposes
    // public Path loadFileAsPath(String uniqueFileNameWithCoursePath) {
    //    // Expects something like "1/xxxx-xxxx-uuid.pdf"
    //    return this.fileStorageLocation.resolve(uniqueFileNameWithCoursePath).normalize();
    // }
} 