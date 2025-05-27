package com.example.inventory.material.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min; // 从全局规范中获取
import java.util.Map;

public class MaterialInputDTO {

    @NotBlank(message = "物资名称不能为空")
    private String materialName;

    @NotNull(message = "分类ID不能为空")
    @Min(value = 1, message = "分类ID无效")
    private Long categoryId;

    private Map<String, Object> specifications;
    private String description;

    // Getters and Setters
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Map<String, Object> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, Object> specifications) {
        this.specifications = specifications;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}