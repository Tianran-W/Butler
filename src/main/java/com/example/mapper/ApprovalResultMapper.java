package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ApprovalResultMapper {
    int updateApprovalStatus(@Param("approval_id") Integer approval_id, @Param("approval_status") String approval_status, @Param("approval_time") java.sql.Timestamp approval_time);
    int updateMaterialStatus(@Param("material_id") Integer material_id, @Param("status") String status);
    int insertUsageRecord(@Param("material_id") Integer material_id, @Param("user_id") Integer user_id,
                          @Param("borrow_time") java.sql.Timestamp borrow_time,
                          @Param("return_time") java.sql.Timestamp return_time,
                          @Param("usage_project") String usage_project);
    Integer getUsageLimit(@Param("material_id") Integer material_id); // 保留获取使用限制的方法
}