package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_material")
public class Material {
    @TableId(type = IdType.AUTO)
    private Integer materialId;
    private String materialName;
    private Integer categoryId;
    private Integer isExpensive;
    private String snCode;
    private Integer quantity;
    private Date usageLimit;
    private String status;
}