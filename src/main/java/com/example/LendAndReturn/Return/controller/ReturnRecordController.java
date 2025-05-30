package com.example.LendAndReturn.Return.controller;

import com.example.LendAndReturn.Return.entity.ReturnRecord;
import com.example.LendAndReturn.Return.service.ReturnRecordService;
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

    // 创建归还记录
    @PostMapping
    public ResponseEntity<String> createReturnRecord(@RequestBody ReturnRecord record) {
        boolean success = returnRecordService.createReturnRecord(record);
        if (success) {
            return new ResponseEntity<>("归还记录成功提交！", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("归还记录提交失败！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 根据借用记录ID获取归还记录列表
    @GetMapping("/{borrowRecordId}")
    public ResponseEntity<List<ReturnRecord>> getReturnRecordsByBorrowRecordId(@PathVariable Long borrowRecordId) {
        List<ReturnRecord> records = returnRecordService.getReturnRecordsByBorrowRecordId(borrowRecordId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // 查询未归还记录
    @GetMapping("/unreturned")
    public ResponseEntity<List<ReturnRecord>> getUnreturnedRecords() {
        List<ReturnRecord> records = returnRecordService.getUnreturnedRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // 根据用户ID查询归还记录列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReturnRecord>> getReturnRecordsByUserId(@PathVariable Long userId) {
        List<ReturnRecord> records = returnRecordService.getReturnRecordsByUserId(userId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // 处理归还请求
    @PutMapping("/process/{recordId}")
    public ResponseEntity<String> processReturn(@PathVariable Long recordId, @RequestParam String photoUrl) {
        boolean success = returnRecordService.processReturn(recordId, photoUrl);
        if (success) {
            return new ResponseEntity<>("归还处理成功！", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("归还处理失败！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}