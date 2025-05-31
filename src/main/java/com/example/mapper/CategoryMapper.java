package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Category;
import com.example.entity.Material;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> selectAllCategories();
    int insertCategory(Category category);

    int insertMaterial(Material material);
}