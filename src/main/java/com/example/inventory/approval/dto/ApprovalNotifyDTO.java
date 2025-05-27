package com.example.inventory.approval.dto;

import jakarta.validation.constraints.NotBlank;

public class ApprovalNotifyDTO {
    @NotBlank(message = "审批ID不能为空")
    private String approvalId;

    @NotBlank(message = "接收用户ID不能为空")
    private String recipientUserId;

    // Getters and Setters
    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public String getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(String recipientUserId) {
        this.recipientUserId = recipientUserId;
    }
}