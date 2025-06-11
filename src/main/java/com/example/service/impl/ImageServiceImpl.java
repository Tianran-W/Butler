package com.example.service.impl;

import com.example.entity.Image;
import com.example.mapper.ImageMapper;
import com.example.service.ImageService;
import com.example.vo.ImageUploadResponseVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Resource
    private ImageMapper imageMapper;

    @Override
    public ImageUploadResponseVO saveImage(MultipartFile file, String recordType, Integer recordId) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        String dailyPath = recordType + File.separator + LocalDate.now().toString();
        File folder = new File(uploadDir + dailyPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = Paths.get(dailyPath, newFileName).toString();
        file.transferTo(new File(uploadDir + filePath));
        
        String webPath = "/" + filePath.replace(File.separator, "/");

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
}