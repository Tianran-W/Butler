package com.example.LendAndReturn.Return.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.LendAndReturn.Return.entity.ReturnRecord;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface ReturnRecordService extends IService<ReturnRecord> {
    // 根据用户ID查询借用物资记录
    List<BorrowRecord> getBorrowRecordsByUserId(Long userId);

    // 用户提交归还记录，通过记录ID定位并修改借用记录
    boolean submitReturnRecord(Long recordId, String photoUrl);
}