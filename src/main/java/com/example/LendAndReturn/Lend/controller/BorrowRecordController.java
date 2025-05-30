package com.example.LendAndReturn.Lend.controller;

import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import com.example.LendAndReturn.Lend.service.BorrowRecordService;
import com.example.LendAndReturn.Lend.dto.BorrowApplyDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class BorrowRecordController {
    private final BorrowRecordService borrowRecordService;

    public BorrowRecordController(BorrowRecordService borrowRecordService) {
        this.borrowRecordService = borrowRecordService;
    }

    @PostMapping("/lend")
    public ResponseEntity<String> createBorrowRecord(@Valid @RequestBody BorrowApplyDTO borrowApplyDTO) {
        BorrowRecord record = new BorrowRecord();
        record.setMaterialId(borrowApplyDTO.getMaterialId());
        record.setUserId(borrowApplyDTO.getUserId());
        record.setProjectUsed(borrowApplyDTO.getProjectUsed());
        record.setBorrowTime(LocalDateTime.now());
        boolean success = borrowRecordService.createBorrowRecord(record);
        if (success) {
            return new ResponseEntity<>("借用记录提交成功！", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("借用记录提交失败！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}/borrow-records")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecordsByUserId(@PathVariable Long userId) {
        List<BorrowRecord> records = borrowRecordService.getBorrowRecordsByUserId(userId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/material/{materialId}/borrow-records")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecordsByMaterialId(@PathVariable Long materialId) {
        List<BorrowRecord> records = borrowRecordService.getBorrowRecordsByMaterialId(materialId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PutMapping("/approve/{recordId}")
    public ResponseEntity<String> approveBorrowRecord(@PathVariable Long recordId,
                                                      @RequestParam String approver,
                                                      @RequestParam String approvalStatus) {
        boolean success = borrowRecordService.approveBorrowRecord(recordId, approver, approvalStatus);
        if (success) {
            return new ResponseEntity<>("借用记录审批成功！", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("借用记录审批失败！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}