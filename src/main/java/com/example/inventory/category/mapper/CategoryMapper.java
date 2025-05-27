package com.example.inventory.category.mapper;

import com.example.inventory.category.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CategoryMapper {
    Category findById(@Param("id") Long id);
    List<Category> findAll(); // 获取所有分类，用于在Java中构建树
    List<Category> findByParentId(@Param("parentId") Long parentId); // 按父ID查找子分类
    
    int insertCategory(Category category);
    int updateCategory(Category category);
    int deleteCategory(@Param("id") Long id);

    // 批量更新排序，可能需要更复杂的SQL或多次单条更新
    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);
    int updateParentAndSortOrder(@Param("id") Long id, @Param("parentId") Long parentId, @Param("sortOrder") Integer sortOrder);

    // 获取指定父ID下的子分类数量
    int countChildrenByParentId(@Param("parentId") Long parentId);

    // 获取指定父ID下，排序大于等于某个值的所有子分类
    List<Category> findChildrenWithSortOrderGreaterThanOrEqual(@Param("parentId") Long parentId, @Param("sortOrder") int sortOrder);
}