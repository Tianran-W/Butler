package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BorrowRequest {
    private Integer material_id;
    private Integer user_id;
    private Integer is_expensive;
    private String usage_project;
    private String approval_reason;
}
