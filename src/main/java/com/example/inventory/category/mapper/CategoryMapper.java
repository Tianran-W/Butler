package com.example.inventory.category.mapper;
import com.example.inventory.category.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface CategoryMapper {
    Category findById(@Param("id") Long id);
    List<Category> findAll();
    List<Category> findByParentId(@Param("parentId") Long parentId);
    int insertCategory(Category category);
    int updateCategory(Category category);
    int deleteCategory(@Param("id") Long id);
    int updateSortOrder(@Param("id") Long id, @Param("sortOrder") Integer sortOrder);
    int updateParentAndSortOrder(@Param("id") Long id, @Param("parentId") Long parentId, @Param("sortOrder") Integer sortOrder);
    int countChildrenByParentId(@Param("parentId") Long parentId);
    List<Category> findChildrenWithSortOrderGreaterThanOrEqual(@Param("parentId") Long parentId, @Param("sortOrder") int sortOrder);
}