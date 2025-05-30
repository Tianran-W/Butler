package com.example.LendAndReturn.Lend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import com.example.LendAndReturn.Lend.mapper.BorrowRecordMapper;
import com.example.LendAndReturn.Lend.service.BorrowRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {
    @Resource
    private BorrowRecordMapper borrowRecordMapper;
    // 实现createBorrowRecord方法
    @Override
    @Transactional
    public boolean createBorrowRecord(BorrowRecord record) {
        // 简单校验，确保关键信息不为空
        if (record.getMaterialId() == null
                || record.getUserId() == null) {
            return false;
        }
        record.setBorrowTime(java.time.LocalDateTime.now());
        int result = borrowRecordMapper.insertWithBorrowTime(record);
        return result > 0;
    }

    // 实现getBorrowRecordsByUserId方法
    @Override
    public List<BorrowRecord> getBorrowRecordsByUserId(Long userId) {
        return borrowRecordMapper.selectByUserIdWithDetail(userId.intValue());
    }

    // 实现getBorrowRecordsByMaterialId方法
    @Override
    public List<BorrowRecord> getBorrowRecordsByMaterialId(Long materialId) {
        return borrowRecordMapper.selectUnreturnedByMaterialId(materialId.intValue());
    }

    // 实现approveBorrowRecord方法
    @Override
    @Transactional
    public boolean approveBorrowRecord(Long recordId, String approver, String approvalStatus) {
        BorrowRecord record = borrowRecordMapper.selectById(recordId);
        if (record == null) {
            return false;
        }
        record.setApprover(approver);
        record.setApprovalStatus(approvalStatus);
        record.setApprovalTime(java.time.LocalDateTime.now());
        int result = borrowRecordMapper.updateById(record);
        return result > 0;
    }

    // 实现saveBatch方法
    @Override
    public boolean saveBatch(Collection<BorrowRecord> entityList, int batchSize) {
        return super.saveBatch(entityList, batchSize);
    }
}