package com.example.inventory.approval.dto;

public class ApprovalNotifyResponseDTO {
    private String messageId;
    private boolean success;

    public ApprovalNotifyResponseDTO(String messageId, boolean success) {
        this.messageId = messageId;
        this.success = success;
    }

    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}