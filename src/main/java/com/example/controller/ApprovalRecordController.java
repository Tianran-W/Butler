package com.example.controller;

import com.example.service.ApprovalRecordService;
import com.example.vo.ApprovalRecordVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ApprovalRecordController {

    @Resource
    private ApprovalRecordService approvalRecordService;

    // 获取等待审批的记录
    @GetMapping("/getApprovalRecord")
    public ResponseEntity<List<ApprovalRecordVO>> getApprovalRecord() {
        List<ApprovalRecordVO> approvalRecordVOs = approvalRecordService.getApprovalRecord();
        return ResponseEntity.status(HttpStatus.OK).body(approvalRecordVOs);
    }
}