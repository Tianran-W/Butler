package com.example.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MaterialVO {
    private Integer materialId;
    private String materialName;
    private String categoryName;
    private Integer isExpensive;
    private String snCode;
    private Integer quantity;
    private Integer usageLimit;
    private String status;
}