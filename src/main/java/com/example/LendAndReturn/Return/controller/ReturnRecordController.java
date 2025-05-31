package com.example.LendAndReturn.Return.controller;

import com.example.LendAndReturn.Return.entity.ReturnRecord;
import com.example.LendAndReturn.Return.service.ReturnRecordService;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/return")
public class ReturnRecordController {
    @Resource
    private ReturnRecordService returnRecordService;

    // 根据用户ID查询借用物资记录
    @GetMapping("/user/{userId}/borrow-records")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecordsByUserId(@PathVariable Long userId) {
        List<BorrowRecord> records = returnRecordService.getBorrowRecordsByUserId(userId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // 用户提交归还记录，通过记录ID定位并修改借用记录
    @PostMapping("/submit/{recordId}")
    public ResponseEntity<String> submitReturnRecord(@PathVariable Long recordId, @RequestParam String photoUrl) {
        boolean success = returnRecordService.submitReturnRecord(recordId, photoUrl);
        if (success) {
            return new ResponseEntity<>("归还记录提交成功！", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("归还记录提交失败！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}