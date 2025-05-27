package com.example.inventory.approval.service;

import com.example.inventory.approval.feishu.FeishuMessageClient;
import com.example.inventory.approval.dto.ApprovalNotifyDTO;
import com.example.inventory.approval.dto.ApprovalNotifyResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApprovalService {

    @Autowired
    private FeishuMessageClient feishuMessageClient;

    @Autowired
    private ApprovalSyncService approvalSyncService; // 用于获取详情等

    public ApprovalNotifyResponseDTO triggerFeishuNotification(ApprovalNotifyDTO notifyDTO) {
        // 1. (可选) 从数据库或其他服务获取更详细的审批信息以构造消息内容
        // com.example.inventory.approval.dto.ApprovalDetailVO approvalDetail = approvalSyncService.getApprovalDetailForFeishu(notifyDTO.getApprovalId());
        // String messageContent = formatMessageContent(approvalDetail);
        
        // 简化：直接使用 FeishuMessageClient 内部逻辑获取内容或传递固定格式内容
        String messageContent = feishuMessageClient.getApprovalDetailsForFeishu(notifyDTO.getApprovalId());

        // 2. 调用飞书客户端发送消息
        try {
            String messageId = feishuMessageClient.sendApprovalNotification(
                    notifyDTO.getApprovalId(),
                    notifyDTO.getRecipientUserId(),
                    messageContent
            );
            // 3. (可选) 记录发送日志或更新状态
            approvalSyncService.syncApprovalStatus(notifyDTO.getApprovalId(), "NOTIFIED_FEISHU", "消息ID: " + messageId);
            return new ApprovalNotifyResponseDTO(messageId, true);
        } catch (Exception e) {
            // 处理发送失败的情况
            // log.error("发送飞书通知失败: {}", e.getMessage());
            approvalSyncService.syncApprovalStatus(notifyDTO.getApprovalId(), "FEISHU_NOTIFICATION_FAILED", e.getMessage());
            return new ApprovalNotifyResponseDTO(null, false);
        }
    }

    // private String formatMessageContent(com.example.inventory.approval.dto.ApprovalDetailVO detail) {
    //     // 根据需求格式化消息内容
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("审批提醒：\n");
    //     sb.append("申请人: ").append(detail.getApplicant()).append("\n");
    //     sb.append("部门: ").append(detail.getDepartment()).append("\n");
    //     sb.append("物资清单:\n");
    //     detail.getMaterials().forEach(m -> 
    //         sb.append("- ").append(m.getName()).append(" (数量: ").append(m.getQuantity()).append(")\n")
    //     );
    //     return sb.toString();
    // }
}