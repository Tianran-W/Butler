package com.example.service.impl;

import com.example.mapper.ApprovalRecordMapper;
import com.example.service.ApprovalRecordService;
import com.example.vo.ApprovalRecordVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class ApprovalRecordServiceImpl implements ApprovalRecordService {

    @Resource
    private ApprovalRecordMapper approvalRecordMapper;

    @Override
    public List<ApprovalRecordVO> getApprovalRecord() {
        return approvalRecordMapper.getApprovalRecord();
    }
}