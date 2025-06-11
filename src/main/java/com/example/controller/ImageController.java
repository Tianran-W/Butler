package com.example.controller;

import com.example.service.ImageService;
import com.example.vo.ImageUploadResponseVO;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ImageController {

    @Resource
    private ImageService imageService;

    @PostMapping("/uploadImage")
    public ResponseEntity<ImageUploadResponseVO> uploadImage(@RequestParam("file") MultipartFile file,
                                                             @RequestParam("recordType") String recordType,
                                                             @RequestParam("recordId") Integer recordId) throws IOException {
        ImageUploadResponseVO response = imageService.saveImage(file, recordType, recordId);
        return ResponseEntity.ok(response);
    }
}