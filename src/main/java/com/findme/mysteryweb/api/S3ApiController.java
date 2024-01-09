package com.findme.mysteryweb.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.findme.mysteryweb.domain.Post;
import com.findme.mysteryweb.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class S3ApiController {

    private final AmazonS3 amazonS3;
    private final ImageService imageService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostMapping("/api/image")
    public ResponseEntity<?> uploadFile(@ModelAttribute UploadFileRequest request) {
        try {
            String originalFilename = request.getFile().getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 현재 날짜와 시간을 기반으로 파일명 생성
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = timeStamp + "-" + originalFilename;

            String fileUrl = "https://detectivesnight.s3.ap-northeast-2.amazonaws.com/" + fileName;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(request.file.getContentType());
            metadata.setContentLength(request.file.getSize());
            amazonS3.putObject(bucket, fileName, request.file.getInputStream(), metadata);

            return ResponseEntity.ok(new Result<>(fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/api/image")
    public ResponseEntity<?> postDelete(@ModelAttribute ImageDeleteRequest request) {
        try {
            amazonS3.deleteObject(bucket, request.filename);

            return ResponseEntity.ok("post delete completed");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file");
        }

    }


    //==DTO==//

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class UploadFileRequest {
        private MultipartFile file;
    }

    @Data
    static class ImageDeleteRequest {
        private String filename;
    }


}