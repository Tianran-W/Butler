package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_image")
public class Image {
    @TableId(type = IdType.AUTO)
    private Integer imageId;
    private String recordType;
    private Integer recordId;
    private String imagePath;
    private LocalDateTime uploadTime;
}