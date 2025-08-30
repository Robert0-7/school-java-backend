package com.hmec.admission_portal.controller;

import com.hmec.admission_portal.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class UploadController {

    @Autowired
    private com.hmec.admission_portal.service.S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String key = s3Service.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully! S3 Key: " + key);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
}