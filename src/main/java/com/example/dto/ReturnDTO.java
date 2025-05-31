package com.example.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ReturnDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer user_id;
    @NotNull(message = "物资ID不能为空")
    private Integer material_id;
}