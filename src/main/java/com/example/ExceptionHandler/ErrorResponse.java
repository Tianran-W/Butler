package com.example.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

// 通用错误响应类
@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String code;
    private String message;
}
