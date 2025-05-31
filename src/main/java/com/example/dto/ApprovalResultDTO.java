package com.example.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ApprovalResultDTO {
    @NotNull(message = "审批ID不能为空")
    private Integer approval_id;
    @NotNull(message = "物资ID不能为空")
    private Integer material_id;
    @NotNull(message = "用户ID不能为空")
    private Integer user_id;
    @NotNull(message = "审批结果不能为空")
    private Boolean approval_result;
    @NotNull(message = "审批原因不能为空")
    private String approval_reason; // 新增字段
}