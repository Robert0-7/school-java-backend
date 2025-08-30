package com.hmec.admission_portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class FileStorageService {

    private final S3Service s3Service;

    @Autowired
    public FileStorageService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String storeFile(MultipartFile file) throws IOException {
        return s3Service.uploadFile(file);
    }
    public Resource loadFileAsResource(String filename) {
        try {
            // Assuming you have a method to get the S3 file URL (modify as per your S3Service)
            URL fileUrl = getS3FileUrl(filename); // Implement this in your S3Service or here
            return new UrlResource(fileUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
    }

    // Example stub for S3 URL retrieval, you should replace this with your actual S3 implementation
    private URL getS3FileUrl(String filename) throws MalformedURLException {
        // Example: Replace with actual logic to generate S3 pre-signed or public URL
        // return s3Service.generatePresignedUrl(filename);
        // For now, just a placeholder that throws
        throw new UnsupportedOperationException("S3 file URL retrieval not implemented!");
    }
}