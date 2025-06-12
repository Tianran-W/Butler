package com.example.dto;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ApprovalResultDTO {
    @NotNull(message = "审批ID不能为空")
    private Integer approvalId;

    @NotNull(message = "物资ID不能为空")
    private Integer materialId;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "审批结果不能为空")
    private Boolean approvalResult;

    private String approvalReason;
}