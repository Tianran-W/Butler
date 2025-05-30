package com.example.LendAndReturn.Lend.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class BorrowApplyDTO {
    @NotNull(message = "物资ID不能为空！")
    private Long materialId;

    @NotNull(message = "用户ID不能为空！")
    private Long userId;

    private String projectUsed;

    // 可以添加一些额外的验证或方法，比如检查项目用途是否符合规范等
    public boolean isValidProjectUsed() {
        return projectUsed != null &&!projectUsed.trim().isEmpty();
    }
}