package com.example.inventory.category.service;

import com.example.inventory.category.dto.CategoryDragDTO;
import com.example.inventory.category.entity.Category;
import com.example.inventory.category.mapper.CategoryMapper;
import com.example.inventory.category.vo.MaterialCategoryVO; // 用于 /api/categories/tree
import com.example.inventory.category.vo.CategoryNodeVO;     // 用于分类管理树
import com.example.inventory.common.exception.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 获取用于 /api/categories/tree 的 MaterialCategoryVO 树形结构
     */
    public List<MaterialCategoryVO> getMaterialCategoryTree() {
        List<Category> allCategories = categoryMapper.findAll(); // 假设按 sortOrder 排序
        Map<Long, List<Category>> parentIdToChildrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        List<MaterialCategoryVO> rootVOs = new ArrayList<>();
        for (Category category : allCategories) {
            if (category.getParentId() == null) {
                rootVOs.add(convertToMaterialCategoryVO(category, parentIdToChildrenMap));
            }
        }
        // 如果数据库没有按sortOrder排序，这里可以再对rootVOs及其子节点排序
        return rootVOs;
    }

    private MaterialCategoryVO convertToMaterialCategoryVO(Category category, Map<Long, List<Category>> parentIdToChildrenMap) {
        MaterialCategoryVO vo = new MaterialCategoryVO(category.getId(), category.getName());
        List<Category> childrenEntities = parentIdToChildrenMap.get(category.getId());
        if (childrenEntities != null && !childrenEntities.isEmpty()) {
            List<MaterialCategoryVO> childrenVOs = childrenEntities.stream()
                    .map(child -> convertToMaterialCategoryVO(child, parentIdToChildrenMap))
                    .collect(Collectors.toList());
            // 如果数据库没有按sortOrder排序，这里可以对childrenVOs排序
            vo.setChildren(childrenVOs);
        } else {
            vo.setChildren(new ArrayList<>()); // 确保children不为null
        }
        return vo;
    }

    /**
     * 获取用于分类管理的 CategoryNodeVO 树形结构
     */
    public List<CategoryNodeVO> getCategoryNodeTree() {
        List<Category> allCategories = categoryMapper.findAll(); // 假设按 sortOrder 排序
         Map<Long, List<Category>> parentIdToChildrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        List<CategoryNodeVO> rootNodes = new ArrayList<>();
        for (Category category : allCategories) {
            if (category.getParentId() == null) {
                rootNodes.add(convertToCategoryNodeVO(category, parentIdToChildrenMap));
            }
        }
        // 如果数据库没有按sortOrder排序，这里可以再对rootNodes及其子节点排序
        return rootNodes;
    }

    private CategoryNodeVO convertToCategoryNodeVO(Category category, Map<Long, List<Category>> parentIdToChildrenMap) {
        CategoryNodeVO node = new CategoryNodeVO(category.getId(), category.getName()); // label用name
        List<Category> childrenEntities = parentIdToChildrenMap.get(category.getId());
        if (childrenEntities != null && !childrenEntities.isEmpty()) {
            List<CategoryNodeVO> childrenNodes = childrenEntities.stream()
                    .map(child -> convertToCategoryNodeVO(child, parentIdToChildrenMap))
                    .collect(Collectors.toList());
            // 如果数据库没有按sortOrder排序，这里可以对childrenNodes排序
            node.setChildren(childrenNodes);
        } else {
            node.setChildren(new ArrayList<>());
        }
        return node;
    }


    @Transactional
    public void handleDragAndDrop(CategoryDragDTO dragDTO) {
        Long draggedNodeId = dragDTO.getDraggedNodeId();
        Long targetParentId = dragDTO.getTargetParentId(); // 可以是 null
        int newIndex = dragDTO.getNewIndex();

        Category draggedNode = categoryMapper.findById(draggedNodeId);
        if (draggedNode == null) {
            throw new ResourceNotFoundException("拖拽的分类节点 " + draggedNodeId + " 不存在");
        }
        if (targetParentId != null && categoryMapper.findById(targetParentId) == null) {
             throw new ResourceNotFoundException("目标父分类节点 " + targetParentId + " 不存在");
        }
        if (Objects.equals(draggedNodeId, targetParentId)) {
            throw new IllegalArgumentException("不能将节点拖拽到自身之下");
        }
        // 防止循环引用 (将父节点拖到子节点下)
        if (targetParentId != null && isDescendant(targetParentId, draggedNodeId)) {
            throw new IllegalArgumentException("不能将父分类拖拽到其子分类下");
        }


        // 1. 从原位置移除 (逻辑上，通过更新受影响节点的 sortOrder)
        // 获取原父节点下的所有兄弟节点
        Long originalParentId = draggedNode.getParentId();
        List<Category> originalSiblings = categoryMapper.findByParentId(originalParentId); // 应该按sortOrder排序
        
        // 从原始兄弟列表中移除被拖拽节点，并更新它们的排序
        int currentIdx = 0;
        for (Category sibling : originalSiblings) {
            if (!Objects.equals(sibling.getId(), draggedNodeId)) {
                if (sibling.getSortOrder() != currentIdx) {
                     categoryMapper.updateSortOrder(sibling.getId(), currentIdx);
                }
                currentIdx++;
            }
        }
        
        // 2. 插入到新位置
        // 获取目标父节点下的所有子节点
        List<Category> targetChildren = categoryMapper.findByParentId(targetParentId); // 应该按sortOrder排序

        // 为新位置腾出空间，更新受影响节点的 sortOrder
        for (int i = targetChildren.size() - 1; i >= newIndex; i--) {
            Category childToShift = targetChildren.get(i);
            categoryMapper.updateSortOrder(childToShift.getId(), i + 1);
        }

        // 3. 更新被拖拽节点的 parentId 和 sortOrder
        categoryMapper.updateParentAndSortOrder(draggedNodeId, targetParentId, newIndex);

        logger.info("分类 {} 已被拖拽到父分类 {} 的位置 {}", draggedNodeId, targetParentId, newIndex);
    }

    // 辅助方法：检查 potentialChildId 是否是 parentId 的后代
    private boolean isDescendant(Long potentialChildId, Long parentId) {
        if (potentialChildId == null || parentId == null) return false;
        Category current = categoryMapper.findById(potentialChildId);
        while (current != null && current.getParentId() != null) {
            if (current.getParentId().equals(parentId)) {
                return true;
            }
            current = categoryMapper.findById(current.getParentId());
        }
        return false;
    }

    // 其他分类管理相关方法，如新增、修改、删除分类等
    @Transactional
    public Category createCategory(String name, Long parentId, Integer sortOrder) {
        if (parentId != null && categoryMapper.findById(parentId) == null) {
            throw new ResourceNotFoundException("父分类ID " + parentId + " 不存在。");
        }
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);

        if (sortOrder == null) {
            // 如果未提供排序，则放到最后
            sortOrder = categoryMapper.countChildrenByParentId(parentId); 
        }
        category.setSortOrder(sortOrder);
        
        categoryMapper.insertCategory(category);
        return category;
    }
}