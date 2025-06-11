package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.ReimbursementRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScrapMapper extends BaseMapper<ReimbursementRelation> {
    void updateMaterialStatusToScrapped(@Param("materialId") Integer materialId, @Param("reason") String reason);
}