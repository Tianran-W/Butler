package com.example.controller;
import com.example.entity.Image;
import com.example.service.ImageService;
import com.example.vo.ImageUploadResponseVO;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
@RestController
@RequestMapping("/api")
public class ImageController {
    @Resource
    private ImageService imageService;
    @PostMapping("/uploadImage")
    public ResponseEntity<ImageUploadResponseVO> uploadImage(@RequestParam("file") MultipartFile file,
                                                             @RequestParam("recordType") String recordType,
                                                             @RequestParam("recordId") String recordId) throws IOException {
        ImageUploadResponseVO response = imageService.saveImage(file, recordType, recordId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/images/{imageId}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadImage(@PathVariable Integer imageId) throws IOException {
        org.springframework.core.io.Resource resource = imageService.downloadImage(imageId);
        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(Path.of(resource.getURI()));
        } catch (IOException e) {
            // ignore
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @GetMapping("/images/record/{recordType}/{recordId}")
    public ResponseEntity<List<Image>> listImagesByRecord(@PathVariable String recordType, @PathVariable String recordId) {
        List<Image> images = imageService.listImagesByRecord(recordType, recordId);
        return ResponseEntity.ok(images);
    }
    @GetMapping("/images/material/{materialId}/{recordType}")
    public ResponseEntity<List<Image>> getImagesByMaterialIdAndRecordType(@PathVariable Integer materialId, @PathVariable String recordType) {
        List<Image> images = imageService.findImagesByMaterialAndRecordType(materialId, recordType);
        return ResponseEntity.ok(images);
    }
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer imageId) throws IOException {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}