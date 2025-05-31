package com.example.inventory.material.mapper;
import com.example.inventory.material.entity.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialMapper {
    int insertMaterial(Material material);
    Material findById(@Param("id") Long id);
    // 添加通过关键字搜索可用物资的方法
    List<Material> selectAvailableMaterialsByKeyword(@Param("keyword") String keyword);

    void updateById(Material material);
}