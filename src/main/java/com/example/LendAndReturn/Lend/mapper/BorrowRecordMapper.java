package com.example.LendAndReturn.Lend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.LendAndReturn.Lend.entity.BorrowRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {
    // 根据用户ID查询借用记录（含关联查询）
    List<BorrowRecord> selectByUserIdWithDetail(@Param("userId") Integer userId);

    // 根据物资ID查询借用记录（未归还状态）
    List<BorrowRecord> selectUnreturnedByMaterialId(@Param("materialId") Integer materialId);

    // 插入借用记录（自动填充借用时间）
    int insertWithBorrowTime(@Param("record") BorrowRecord record);
}