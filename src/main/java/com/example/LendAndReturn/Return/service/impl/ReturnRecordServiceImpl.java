package com.example.LendAndReturn.Return.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.LendAndReturn.Return.entity.ReturnRecord;
import com.example.LendAndReturn.Return.mapper.ReturnRecordMapper;
import com.example.LendAndReturn.Return.service.ReturnRecordService;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import com.example.LendAndReturn.Lend.mapper.BorrowRecordMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class ReturnRecordServiceImpl extends ServiceImpl<ReturnRecordMapper, ReturnRecord>
        implements ReturnRecordService {

    @Resource
    private BorrowRecordMapper borrowRecordMapper;

    @Override
    public List<BorrowRecord> getBorrowRecordsByUserId(Long userId) {
        return borrowRecordMapper.selectByUserIdWithDetail(userId.intValue());
    }

    @Override
    public boolean submitReturnRecord(Long recordId, String photoUrl) {
        BorrowRecord borrowRecord = borrowRecordMapper.selectById(recordId);
        if (borrowRecord == null) {
            return false;
        }

        ReturnRecord returnRecord = new ReturnRecord();
        returnRecord.setBorrowRecordId(recordId);
        returnRecord.setMaterialId(borrowRecord.getMaterialId());
        returnRecord.setUserId(borrowRecord.getUserId());
        returnRecord.setReturnTime(java.time.LocalDateTime.now());
        returnRecord.setReturnStatus("正常归还");
        returnRecord.setPhotoUrl(photoUrl);

        // 修正：先设置状态，再检查更新结果
        borrowRecord.setApprovalStatus("已归还");
        return save(returnRecord) && borrowRecordMapper.updateById(borrowRecord) > 0;
    }
}