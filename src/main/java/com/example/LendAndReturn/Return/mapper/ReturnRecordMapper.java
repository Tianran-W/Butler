package com.example.LendAndReturn.Return.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.LendAndReturn.Return.entity.ReturnRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReturnRecordMapper extends BaseMapper<ReturnRecord> {
    // 根据借用记录ID查询归还记录
    List<ReturnRecord> selectByBorrowRecordId(@Param("borrowRecordId") Long borrowRecordId);

    // 查询未归还记录（return_time为空）
    List<ReturnRecord> selectUnreturnedRecords();

    // 根据用户ID查询归还记录
    List<ReturnRecord> selectByUserId(@Param("userId") Long userId);
}