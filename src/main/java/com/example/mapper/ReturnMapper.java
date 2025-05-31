package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReturnMapper {
    // 更新物资使用记录表的归还时间
    int updateReturnTime(@Param("material_id") Integer material_id);
    // 更新物资表的状态
    int updateMaterialStatus(@Param("material_id") Integer material_id);
}