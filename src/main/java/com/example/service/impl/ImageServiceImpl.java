package com.example.service.impl;

import com.example.entity.Image;
import com.example.mapper.ImageMapper;
import com.example.service.ImageService;
import com.example.vo.ImageUploadResponseVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Resource
    private ImageMapper imageMapper;

    @Override
    @Transactional
    public ImageUploadResponseVO saveImage(MultipartFile file, String recordType, Integer recordId) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        String dailyPath = Paths.get(recordType, LocalDate.now().toString()).toString();
        Path uploadPath = Paths.get(this.uploadDir);
        Path dailyUploadPath = uploadPath.resolve(dailyPath);

        if (!Files.exists(dailyUploadPath)) {
            Files.createDirectories(dailyUploadPath);
        }

        Path destinationFile = dailyUploadPath.resolve(newFileName);
        file.transferTo(destinationFile);

        String webPath = "/" + Paths.get(dailyPath).resolve(newFileName).toString().replace(File.separator, "/");

        Image image = new Image();
        image.setRecordType(recordType);
        image.setRecordId(recordId);
        image.setImagePath(webPath);
        image.setUploadTime(LocalDateTime.now());
        imageMapper.insert(image);

        ImageUploadResponseVO responseVO = new ImageUploadResponseVO();
        responseVO.setImageId(image.getImageId());
        responseVO.setImagePath(webPath);
        return responseVO;
    }

    @Override
    public org.springframework.core.io.Resource downloadImage(Integer imageId) throws IOException {
        Image image = imageMapper.selectById(imageId);
        if (image == null) {
            throw new RuntimeException("Image not found with id: " + imageId);
        }
        Path filePath = Paths.get(uploadDir).resolve(image.getImagePath().substring(1)).normalize();
        org.springframework.core.io.Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("File path is invalid: " + image.getImagePath(), e);
        }

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Could not read file: " + image.getImagePath());
        }
        return resource;
    }

    @Override
    @Transactional
    public void deleteImage(Integer imageId) throws IOException {
        Image image = imageMapper.selectById(imageId);
        if (image == null) {
            // 如果图片不存在，直接返回成功，保持幂等性
            return;
        }
        Path filePath = Paths.get(uploadDir).resolve(image.getImagePath().substring(1)).normalize();
        imageMapper.deleteById(imageId);
        Files.deleteIfExists(filePath);
    }

    @Override
    public List<Image> listImagesByRecord(String recordType, Integer recordId) {
        return imageMapper.findByRecordTypeAndRecordId(recordType, recordId);
    }
}