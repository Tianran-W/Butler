package com.example.LendAndReturn.Lend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import com.example.LendAndReturn.Lend.mapper.BorrowRecordMapper;
import com.example.LendAndReturn.Lend.service.BorrowRecordService;
import com.example.inventory.material.entity.Material;
import com.example.inventory.material.mapper.MaterialMapper;
import com.example.inventory.approval.mapper.ApprovalMapper;
import com.example.inventory.approval.entity.Approval;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {
    @Resource
    private BorrowRecordMapper borrowRecordMapper;
    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private ApprovalMapper approvalMapper;

    @Override
    @Transactional
    public boolean createBorrowRecord(BorrowRecord record, Material material) {
        if (record.getMaterialId() == null || record.getUserId() == null) {
            return false;
        }
        record.setBorrowTime(LocalDateTime.now());

        if (material.getIsExpensive() == 1) {
            // 贵重物资，添加审批申请
            Approval approval = new Approval();
            approval.setMaterialId(material.getMaterialId());
            approval.setUserId(record.getUserId());
            approval.setApprovalReason("用户申请借用物资");
            approval.setApprovalStatus("待审批");
            approval.setApprovalTime(LocalDateTime.now());
            approvalMapper.insert(approval);

            // 修改物资状态为审批中
            material.setStatus("审批中");
            materialMapper.updateById(material);
        } else {
            // 非贵重物资，直接新增借用记录
            int result = borrowRecordMapper.insertWithBorrowTime(record);
            if (result > 0) {
                // 修改物资状态为借出
                material.setStatus("借出");
                materialMapper.updateById(material);
            }
            return result > 0;
        }
        return true;
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByUserId(Long userId) {
        return borrowRecordMapper.selectByUserIdWithDetail(userId.intValue());
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByMaterialId(Long materialId) {
        return borrowRecordMapper.selectUnreturnedByMaterialId(materialId.intValue());
    }

    @Override
    @Transactional
    public boolean approveBorrowRecord(Long recordId, String approver, String approvalStatus) {
        BorrowRecord record = borrowRecordMapper.selectById(recordId);
        if (record == null) {
            return false;
        }
        record.setApprover(approver);
        record.setApprovalStatus(approvalStatus);
        record.setApprovalTime(LocalDateTime.now());
        int result = borrowRecordMapper.updateById(record);
        return result > 0;
    }

    @Override
    public boolean saveBatch(Collection<BorrowRecord> entityList, int batchSize) {
        return super.saveBatch(entityList, batchSize);
    }
}
