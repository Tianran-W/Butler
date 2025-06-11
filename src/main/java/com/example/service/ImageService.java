package com.example.service;

import com.example.vo.ImageUploadResponseVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    ImageUploadResponseVO saveImage(MultipartFile file, String recordType, Integer recordId) throws IOException;
}