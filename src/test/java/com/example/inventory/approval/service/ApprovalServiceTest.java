package com.example.inventory.approval.service;

import com.example.inventory.approval.dto.ApprovalNotifyDTO;
import com.example.inventory.approval.dto.ApprovalNotifyResponseDTO;
import com.example.inventory.approval.feishu.FeishuMessageClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalServiceTest {

    @Mock
    private FeishuMessageClient feishuMessageClient;

    @Mock
    private ApprovalSyncService approvalSyncService;

    @InjectMocks
    private ApprovalService approvalService;

    @Test
    void triggerFeishuNotification_Success() {
        // Arrange
        ApprovalNotifyDTO notifyDTO = new ApprovalNotifyDTO();
        notifyDTO.setApprovalId("app-123");
        notifyDTO.setRecipientUserId("user-456");
        
        when(feishuMessageClient.getApprovalDetailsForFeishu("app-123"))
            .thenReturn("审批详情内容");
        when(feishuMessageClient.sendApprovalNotification("app-123", "user-456", "审批详情内容"))
            .thenReturn("msg-789");

        // Act
        ApprovalNotifyResponseDTO response = approvalService.triggerFeishuNotification(notifyDTO);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("msg-789", response.getMessageId());
        verify(approvalSyncService).syncApprovalStatus("app-123", "NOTIFIED_FEISHU", "消息ID: msg-789");
    }

    @Test
    void triggerFeishuNotification_Failure() {
        // Arrange
        ApprovalNotifyDTO notifyDTO = new ApprovalNotifyDTO();
        notifyDTO.setApprovalId("app-123");
        notifyDTO.setRecipientUserId("user-456");
        
        when(feishuMessageClient.getApprovalDetailsForFeishu("app-123"))
            .thenReturn("审批详情内容");
        when(feishuMessageClient.sendApprovalNotification(eq("app-123"), eq("user-456"), anyString()))
            .thenThrow(new RuntimeException("飞书服务不可用"));

        // Act
        ApprovalNotifyResponseDTO response = approvalService.triggerFeishuNotification(notifyDTO);

        // Assert
        assertFalse(response.isSuccess());
        assertNull(response.getMessageId());
        verify(approvalSyncService).syncApprovalStatus("app-123", "FEISHU_NOTIFICATION_FAILED", "java.lang.RuntimeException: 飞书服务不可用");
    }
}