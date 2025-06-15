package com.example.vo;

import javax.print.DocFlavor.STRING;

import lombok.Data;

@Data
public class RecommendedMaterialVO {
    private Integer materialId;
    private String materialName;
    private String recommendReason;
    private String avgUsage;
}