package com.example.inventory.approval.feishu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeishuMessageClientTest {

    private FeishuMessageClient client = new FeishuMessageClient();

    @Test
    void sendApprovalNotification() {
        // Act
        String messageId = client.sendApprovalNotification("app-123", "user-456", "Test message");
        
        // Assert
        assertNotNull(messageId);
        assertTrue(messageId.startsWith("msg_"));
        assertTrue(messageId.length() > 4);
    }

    @Test
    void getApprovalDetailsForFeishu() {
        // Act
        String details = client.getApprovalDetailsForFeishu("app-456");
        
        // Assert
        assertNotNull(details);
        assertTrue(details.contains("审批ID: app-456"));
        assertTrue(details.contains("MacBook Pro"));
        assertTrue(details.contains("张三"));
    }
}