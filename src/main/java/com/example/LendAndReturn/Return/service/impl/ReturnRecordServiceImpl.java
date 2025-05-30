package com.example.LendAndReturn.Return.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.LendAndReturn.Return.entity.ReturnRecord;
import com.example.LendAndReturn.Return.mapper.ReturnRecordMapper;
import com.example.LendAndReturn.Return.service.ReturnRecordService;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import com.example.LendAndReturn.Lend.mapper.BorrowRecordMapper;
import com.example.inventory.material.service.MaterialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReturnRecordServiceImpl extends ServiceImpl<ReturnRecordMapper, ReturnRecord> implements ReturnRecordService {
    @Resource
    private ReturnRecordMapper returnRecordMapper;
    @Resource
    private BorrowRecordMapper borrowRecordMapper;
    @Resource
    private MaterialService materialService;

    @Override
    @Transactional
    public boolean createReturnRecord(ReturnRecord record) {
        // 简单校验，确保关键信息不为空
        if (record.getBorrowRecordId() == null
                || record.getMaterialId() == null
                || record.getUserId() == null) {
            return false;
        }
        record.setReturnTime(LocalDateTime.now());
        int result = returnRecordMapper.insert(record);
        return result > 0;
    }

    @Override
    public List<ReturnRecord> getReturnRecordsByBorrowRecordId(Long borrowRecordId) {
        return returnRecordMapper.selectByBorrowRecordId(borrowRecordId);
    }

    @Override
    public List<ReturnRecord> getUnreturnedRecords() {
        return returnRecordMapper.selectUnreturnedRecords();
    }

    @Override
    public List<ReturnRecord> getReturnRecordsByUserId(Long userId) {
        return returnRecordMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public boolean processReturn(Long recordId, String photoUrl) {
        // 1. 查询借用记录是否存在
        BorrowRecord borrowRecord = borrowRecordMapper.selectById(recordId);
        if (borrowRecord == null) {
            return false;
        }

        // 2. 更新归还记录
        ReturnRecord returnRecord = new ReturnRecord();
        returnRecord.setId(recordId);
        returnRecord.setReturnTime(LocalDateTime.now());
        returnRecord.setPhotoUrl(photoUrl);
        returnRecord.setReturnStatus("正常归还"); // 可根据实际情况调整
        int result = returnRecordMapper.updateById(returnRecord);

        // 3. 更新物资状态为"可用"并恢复库存
//        Material material = materialService.getById(borrowRecord.getMaterialId());
//        if (material != null) {
//            material.setStatus("可用");
//            material.setQuantity(material.getQuantity() + 1);
//            materialService.updateById(material);
//        }

        return result > 0;
    }
}