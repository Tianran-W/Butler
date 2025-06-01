package com.example.controller;

import com.example.entity.BorrowRequest;
import com.example.service.BorrowService;
import com.example.vo.MaterialVO;
import com.example.dto.UserBorrowingQueryDTO;
import com.example.dto.BorrowDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowController {

    @Resource
    private BorrowService borrowService;

    // 新建借用记录
    @PostMapping("/addNewBorrow")
    public ResponseEntity<Void> addNewBorrow(@Valid @RequestBody BorrowDTO borrowDTO) {
        // 将DTO转换为业务实体
        BorrowRequest borrowRequest = new BorrowRequest();
        borrowRequest.setMaterial_id(borrowDTO.getMaterialId());
        borrowRequest.setUser_id(borrowDTO.getUserId());
        borrowRequest.setIs_expensive(borrowDTO.getIsExpensive());
        borrowRequest.setUsage_project(borrowDTO.getUsageProject());
        borrowRequest.setApproval_reason(borrowDTO.getApprovalReason());

        borrowService.addNewBorrow(borrowRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //通过用户Id搜索相关的借用
    @PostMapping("/findBorrowingByUserId")
    public ResponseEntity<List<MaterialVO>> findUserBorrowing(
            @Valid @RequestBody UserBorrowingQueryDTO queryDTO) {
        List<MaterialVO> materialVOs = borrowService.findUserBorrowing(queryDTO.getUserId());
        return ResponseEntity.ok(materialVOs);
    }
}