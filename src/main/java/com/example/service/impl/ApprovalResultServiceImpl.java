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
        LocalDateTime now = LocalDateTime.now();
        Timestamp approvalTime = Timestamp.valueOf(now);
        Timestamp borrowTime = Timestamp.valueOf(now);

        String approvalStatus = dto.getApprovalResult() ? "通过" : "拒绝";
        String materialStatus = dto.getApprovalResult() ? "已借出" : "在库可借";

        approvalResultMapper.updateApprovalStatus(
                dto.getApprovalId(),
                approvalStatus,
                approvalTime
        );

        approvalResultMapper.updateMaterialStatus(
                dto.getMaterialId(),
                materialStatus
        );

        Integer usageLimit = approvalResultMapper.getUsageLimit(dto.getMaterialId());
        Timestamp returnTime = null;
        if (usageLimit != null) {
            returnTime = Timestamp.valueOf(now.plus(usageLimit, ChronoUnit.DAYS));
        }

        approvalResultMapper.insertUsageRecord(
                dto.getMaterialId(),
                dto.getUserId(),
                borrowTime,
                returnTime,
                dto.getApprovalReason()
        );
    }
}