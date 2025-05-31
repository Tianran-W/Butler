package com.example.service.impl;

import com.example.dto.ApprovalResultDTO;
import com.example.mapper.ApprovalResultMapper;
import com.example.service.ApprovalResultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ApprovalResultServiceImpl implements ApprovalResultService {

    @Resource
    private ApprovalResultMapper approvalResultMapper;

    @Override
    @Transactional
    public void handleApprovalResult(ApprovalResultDTO dto) {
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        Timestamp approvalTime = Timestamp.valueOf(now);
        Timestamp borrowTime = Timestamp.valueOf(now);

        // 根据审批结果设置状态
        String approvalStatus = dto.getApproval_result() ? "通过" : "拒绝";
        String materialStatus = dto.getApproval_result() ? "已借出" : "在库可借";

        // 1. 更新审批表状态
        approvalResultMapper.updateApprovalStatus(
                dto.getApproval_id(),
                approvalStatus,
                approvalTime
        );

        // 2. 更新物资表状态
        approvalResultMapper.updateMaterialStatus(
                dto.getMaterial_id(),
                materialStatus
        );

        // 3. 计算归还时间（如果有使用限制）
        Integer usageLimit = approvalResultMapper.getUsageLimit(dto.getMaterial_id());
        Timestamp returnTime = null;
        if (usageLimit != null) {
            returnTime = Timestamp.valueOf(now.plus(usageLimit, ChronoUnit.DAYS));
        }

        // 4. 插入物资使用记录（使用传入的 approval_reason 作为 usage_project）
        approvalResultMapper.insertUsageRecord(
                dto.getMaterial_id(),
                dto.getUser_id(),
                borrowTime,
                returnTime,
                dto.getApproval_reason() // 直接使用传入的审批原因
        );
    }
}