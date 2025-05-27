package com.example.inventory.approval.entity;

import java.time.LocalDateTime;

public class ApprovalStatusSync {
    private Long id; // 自增主键
    private String approvalId; // 审批实例ID
    private String status; // 同步的状态
    private LocalDateTime syncTimestamp; // 同步时间戳
    private String details; // 其他详情，可选

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSyncTimestamp() {
        return syncTimestamp;
    }

    public void setSyncTimestamp(LocalDateTime syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}