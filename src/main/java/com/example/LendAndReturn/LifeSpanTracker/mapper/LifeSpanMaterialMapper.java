package com.example.LendAndReturn.LifeSpanTracker.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.LendAndReturn.LifeSpanTracker.entity.LifeSpanMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LifeSpanMaterialMapper extends BaseMapper<LifeSpanMaterial> {
    List<LifeSpanMaterial> selectByKeyword(@Param("keyword") String keyword);
    List<LifeSpanMaterial> getAll();
}