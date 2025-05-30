package com.example.inventory.approval.service;

import com.example.inventory.approval.dto.ApprovalDetailVO;
import com.example.inventory.approval.vo.MaterialBriefVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApprovalSyncServiceTest {

    @InjectMocks
    private ApprovalSyncService approvalSyncService;

    @Test
    void getApprovalDetailForPopup() {
        // Act
        ApprovalDetailVO detailVO = approvalSyncService.getApprovalDetailForPopup("app-123");
        
        // Assert
        assertNotNull(detailVO);
        assertEquals("模拟申请人-张三", detailVO.getApplicant());
        assertEquals("模拟部门-技术部", detailVO.getDepartment());
        assertNotNull(detailVO.getMaterials());
        assertEquals(2, detailVO.getMaterials().size());
        
        MaterialBriefVO material1 = detailVO.getMaterials().get(0);
        assertEquals(123L, material1.getId());
        assertEquals("投影仪", material1.getName());
        assertEquals(1, material1.getQuantity());
        
        MaterialBriefVO material2 = detailVO.getMaterials().get(1);
        assertEquals(124L, material2.getId());
        assertEquals("白板笔", material2.getName());
        assertEquals(10, material2.getQuantity());
    }
}