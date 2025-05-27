package com.example.inventory.material.mapper;

import com.example.inventory.material.entity.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaterialMapper {
    int insertMaterial(Material material);
    Material findById(@Param("id") Long id);
    // 其他CRUD方法
}