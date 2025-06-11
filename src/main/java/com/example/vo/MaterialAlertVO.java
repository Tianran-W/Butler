package com.example.vo;

import lombok.Data;

@Data
public class MaterialAlertVO {
    private Integer materialId;
    private String materialName;
    private Integer currentQuantity;
    private Integer alertThreshold;
}