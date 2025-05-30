package com.example.LendAndReturn.Lend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;

import java.util.List;

/**
 * 借出记录服务接口，定义借出业务相关方法
 */
public interface BorrowRecordService extends IService<BorrowRecord> {
    /**
     * 创建借用记录
     * @param record 借用记录实体
     * @return 创建成功返回true，否则返回false
     */
    boolean createBorrowRecord(BorrowRecord record);

    /**
     * 根据用户ID获取借用记录列表
     * @param userId 用户ID
     * @return 用户的借用记录列表
     */
    List<BorrowRecord> getBorrowRecordsByUserId(Long userId);

    /**
     * 根据物资ID获取借用记录列表
     * @param materialId 物资ID
     * @return 物资的借用记录列表
     */
    List<BorrowRecord> getBorrowRecordsByMaterialId(Long materialId);

    /**
     * 审批借用记录
     * @param recordId 借用记录ID
     * @param approver 审批人
     * @param approvalStatus 审批状态（待审批/通过/拒绝/已归还）
     * @return 审批成功返回true，否则返回false
     */
    boolean approveBorrowRecord(Long recordId, String approver, String approvalStatus);
}