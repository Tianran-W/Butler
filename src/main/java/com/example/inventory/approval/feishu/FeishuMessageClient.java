package com.example.inventory.approval.feishu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

// 这是一个模拟的飞书客户端
@Component
public class FeishuMessageClient {

    private static final Logger logger = LoggerFactory.getLogger(FeishuMessageClient.class);

    // 模拟发送飞书消息
    // 实际实现中，这里会调用飞书的API
    public String sendApprovalNotification(String approvalId, String recipientUserId, String messageContent) {
        // 实际的API调用逻辑
        // 例如：使用HTTP Client发送请求到飞书接口
        logger.info("向用户 {} 推送飞书审批通知，审批ID: {}, 内容: {}", recipientUserId, approvalId, messageContent);
        
        // 模拟返回一个消息ID
        String messageId = "msg_" + UUID.randomUUID().toString().substring(0, 8);
        logger.info("飞书消息发送成功，消息ID: {}", messageId);
        return messageId; 
    }

    // 模拟获取审批详情以构建消息内容
    // 实际中，这可能从你的业务数据库或飞书审批详情API获取
    public String getApprovalDetailsForFeishu(String approvalId) {
        // 假设根据approvalId查询数据库或其他服务得到审批详情
        // 这里只是一个示例
        return "审批ID: " + approvalId + " - 内容：请尽快处理MacBook Pro采购申请。申请人：张三，部门：技术部。";
    }
}