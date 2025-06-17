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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ImageServiceImpl implements ImageService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Resource
    private ImageMapper imageMapper;
    private static final String URL_PREFIX = "/uploads/";
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    @Override
    @Transactional
    public ImageUploadResponseVO saveImage(MultipartFile file, String recordType, String recordId) throws IOException {
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
        String relativePath = Paths.get(dailyPath).resolve(newFileName).toString().replace(File.separator, "/");
        String webPath = URL_PREFIX + relativePath;
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
        String imagePath = image.getImagePath();
        if (!imagePath.startsWith(URL_PREFIX)) {
            throw new RuntimeException("Invalid image path format: " + imagePath);
        }
        String relativePath = imagePath.substring(URL_PREFIX.length());
        Path filePath = Paths.get(uploadDir).resolve(relativePath).normalize();
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
            return;
        }
        String imagePath = image.getImagePath();
        if (!imagePath.startsWith(URL_PREFIX)) {
            imageMapper.deleteById(imageId);
            return;
        }
        String relativePath = imagePath.substring(URL_PREFIX.length());
        Path filePath = Paths.get(uploadDir).resolve(relativePath).normalize();
        imageMapper.deleteById(imageId);
        Files.deleteIfExists(filePath);
    }
    @Override
    public List<Image> listImagesByRecord(String recordType, String recordId) {
        return imageMapper.findByRecordTypeAndRecordId(recordType, recordId);
    }
    @Override
    public List<Image> findImagesByMaterialAndRecordType(Integer materialId, String recordType) {
        return imageMapper.findByRecordTypeAndRecordId(recordType, String.valueOf(materialId));
    }
}