package com.example.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class UserBorrowingQueryDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer userId;
}