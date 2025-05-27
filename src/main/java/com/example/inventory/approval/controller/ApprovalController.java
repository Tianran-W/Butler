package com.example.inventory.approval.controller;

import com.example.inventory.approval.dto.ApprovalNotifyDTO;
import com.example.inventory.approval.dto.ApprovalNotifyResponseDTO;
import com.example.inventory.approval.service.ApprovalService;
import com.example.inventory.approval.dto.ApprovalDetailVO; // 用于未来可能的弹窗详情接口
import com.example.inventory.approval.service.ApprovalSyncService; // 用于未来可能的弹窗详情接口
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/approval")
public class ApprovalController {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalController.class);
    
    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private ApprovalSyncService approvalSyncService; // 注入用于获取弹窗详情

    @PostMapping("/notify")
    public ResponseEntity<ApprovalNotifyResponseDTO> notifyFeishu(@Valid @RequestBody ApprovalNotifyDTO notifyDTO) {
        logger.info("接收到飞书通知请求: approvalId={}, recipientUserId={}", notifyDTO.getApprovalId(), notifyDTO.getRecipientUserId());
        ApprovalNotifyResponseDTO response = approvalService.triggerFeishuNotification(notifyDTO);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            // 可以根据具体错误返回不同状态码，这里简化处理
            return ResponseEntity.status(500).body(response);
        }
    }

    // 示例：一个根据审批ID获取弹窗所需详情的接口 (实际业务中可能会有)
    @GetMapping("/{approvalId}/details")
    public ResponseEntity<ApprovalDetailVO> getApprovalPopupDetails(@PathVariable String approvalId) {
        logger.info("请求获取审批ID {} 的弹窗详情", approvalId);
        ApprovalDetailVO detailVO = approvalSyncService.getApprovalDetailForPopup(approvalId);
        // 这里可以加入权限校验等逻辑
        if (detailVO == null) {
            // 如果找不到，可以返回404，这里假设总能找到模拟数据
            // throw new ResourceNotFoundException("审批详情未找到: " + approvalId);
        }
        return ResponseEntity.ok(detailVO);
    }
}