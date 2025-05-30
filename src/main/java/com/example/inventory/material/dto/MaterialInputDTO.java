package com.example.inventory.material.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
public class MaterialInputDTO {
    @NotBlank(message = "物资名称不能为空")
    private String material_name;
    @NotNull(message = "分类ID不能为空")
    @Min(value = 1, message = "分类ID无效")
    private Long category_id;

    public String getMaterial_name() {
        return material_name;
    }
    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }
    public Long getCategory_id() {
        return category_id;
    }
    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
}