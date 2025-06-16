package com.example.service;

import com.example.entity.Image;
import com.example.vo.ImageUploadResponseVO;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface ImageService {
    ImageUploadResponseVO saveImage(MultipartFile file, String recordType, Integer recordId) throws IOException;

    org.springframework.core.io.Resource downloadImage(Integer imageId) throws IOException;

    void deleteImage(Integer imageId) throws IOException;

    List<Image> listImagesByRecord(String recordType, Integer recordId);
}