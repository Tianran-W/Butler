package com.example.controller;

import com.example.dto.ApprovalResultDTO;
import com.example.service.ApprovalResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/admin")
public class ApprovalResultController {

    @Resource
    private ApprovalResultService approvalResultService;

    @PostMapping("/ApprovalResult")
    public ResponseEntity<Void> handleApprovalResult(@Valid @RequestBody ApprovalResultDTO dto) {
        approvalResultService.handleApprovalResult(dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}