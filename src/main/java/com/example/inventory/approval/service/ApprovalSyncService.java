package com.example.inventory.approval.service;

import com.example.inventory.approval.entity.ApprovalStatusSync;
// import com.example.inventory.approval.mapper.ApprovalStatusSyncMapper; // 假设有这个Mapper
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ApprovalSyncService {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalSyncService.class);

    // @Autowired
    // private ApprovalStatusSyncMapper approvalStatusSyncMapper; // 注入Mapper

    // 模拟同步审批状态
    @Transactional
    public void syncApprovalStatus(String approvalId, String status, String details) {
        ApprovalStatusSync syncRecord = new ApprovalStatusSync();
        syncRecord.setApprovalId(approvalId);
        syncRecord.setStatus(status);
        syncRecord.setSyncTimestamp(LocalDateTime.now());
        syncRecord.setDetails(details);

        // approvalStatusSyncMapper.insert(syncRecord); // 保存到数据库
        logger.info("审批状态同步记录已保存: approvalId={}, status={}", approvalId, status);
        // 实际业务逻辑，例如根据状态更新相关物资的可用性等
    }

    // 模拟获取审批详情，用于弹窗显示
    // 实际应用中，这可能需要查询多个表（用户、部门、申请的物资等）
    public com.example.inventory.approval.dto.ApprovalDetailVO getApprovalDetailForPopup(String approvalId) {
        logger.info("正在获取审批ID {} 的详情用于弹窗显示...", approvalId);
        // 此处应有真实的数据获取逻辑
        // 以下为模拟数据
        com.example.inventory.approval.dto.ApprovalDetailVO detailVO = new com.example.inventory.approval.dto.ApprovalDetailVO();
        detailVO.setApplicant("模拟申请人-张三");
        detailVO.setDepartment("模拟部门-技术部");
        
        java.util.List<com.example.inventory.approval.vo.MaterialBriefVO> materials = new java.util.ArrayList<>();
        materials.add(new com.example.inventory.approval.vo.MaterialBriefVO(123L, "投影仪", 1));
        materials.add(new com.example.inventory.approval.vo.MaterialBriefVO(124L, "白板笔", 10));
        detailVO.setMaterials(materials);

        return detailVO;
    }
}