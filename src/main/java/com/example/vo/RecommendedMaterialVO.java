package com.example.vo;

import lombok.Data;

@Data
public class RecommendedMaterialVO {
    private Integer materialId;
    private String materialName;
    private String recommendReason;
    private Double avgUsage;
}