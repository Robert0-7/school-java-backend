import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class UploadController {

    private final S3Client s3Client;

    public UploadController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("enquiryNumber") String enquiryNumber,
            @RequestParam("type") String type) throws IOException {

        String fileName = type + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String key = "enquiries/" + enquiryNumber + "/" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("my-admission-portal-uploads")
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        return "https://my-admission-portal-uploads.s3.ap-south-1.amazonaws.com/" + key;
    }
}
