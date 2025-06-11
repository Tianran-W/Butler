package com.example.vo;

import lombok.Data;

@Data
public class LoginResponseVO {
    private String message;
    private Integer userId;
    private String username;
    private String role;
}