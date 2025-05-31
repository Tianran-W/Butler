package com.example.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class BorrowDTO {
    @NotNull(message = "物资ID不能为空")
    private Integer materialId;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "是否贵重不能为空")
    private Integer isExpensive;

    private String usageProject;
    private String approvalReason;
}