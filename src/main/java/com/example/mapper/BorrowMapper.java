package com.example.mapper;

import com.example.entity.BorrowRequest;
import com.example.vo.MaterialVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BorrowMapper {
    // 插入物资使用记录
    int insertUsageRecord(BorrowRequest borrowRequest);
    // 插入审批记录
    int insertApprovalRecord(BorrowRequest borrowRequest);
    // 更新物资状态
    int updateMaterialStatus(Integer materialId, String status);
    // 根据用户ID查询借用的物资信息
    List<MaterialVO> findUserBorrowing(Integer user_id);
}