package com.example.LendAndReturn.Lend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import com.example.inventory.material.entity.Material;

import java.util.List;

public interface BorrowRecordService extends IService<BorrowRecord> {
    boolean createBorrowRecord(BorrowRecord record, Material material);
    List<BorrowRecord> getBorrowRecordsByUserId(Long userId);
    List<BorrowRecord> getBorrowRecordsByMaterialId(Long materialId);
    boolean approveBorrowRecord(Long recordId, String approver, String approvalStatus);
}
