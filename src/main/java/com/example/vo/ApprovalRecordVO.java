package com.example.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ApprovalRecordVO {
    private Integer materialId;
    private Integer userId;
    private String approvalReason;
    private String approvalStatus;
    private String username;
    private String materialName;
    private Integer approvalId; // 新增字段
}