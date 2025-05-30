package com.example.LendAndReturn.Return.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.LendAndReturn.Return.entity.ReturnRecord;

import java.util.List;

public interface ReturnRecordService extends IService<ReturnRecord> {
    // 创建归还记录
    boolean createReturnRecord(ReturnRecord record);

    // 根据借用记录ID获取归还记录列表
    List<ReturnRecord> getReturnRecordsByBorrowRecordId(Long borrowRecordId);

    // 查询未归还记录（用于提醒）
    List<ReturnRecord> getUnreturnedRecords();

    // 根据用户ID查询归还记录列表
    List<ReturnRecord> getReturnRecordsByUserId(Long userId);

    // 处理归还请求
    boolean processReturn(Long recordId, String photoUrl);
}