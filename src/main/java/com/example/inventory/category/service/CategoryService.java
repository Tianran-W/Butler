package com.example.inventory.category.service;
import com.example.inventory.category.dto.CategoryDragDTO;
import com.example.inventory.category.entity.Category;
import com.example.inventory.category.mapper.CategoryMapper;
import com.example.inventory.category.vo.MaterialCategoryVO;
import com.example.inventory.category.vo.CategoryNodeVO;
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
    public List<MaterialCategoryVO> getMaterialCategoryTree() {
        List<Category> allCategories = categoryMapper.findAll();
        Map<Long, List<Category>> parentIdToChildrenMap = allCategories.stream()
                .filter(c -> c.getParent_id() != null)
                .collect(Collectors.groupingBy(Category::getParent_id));
        List<MaterialCategoryVO> rootVOs = new ArrayList<>();
        for (Category category : allCategories) {
            if (category.getParent_id() == null) {
                rootVOs.add(convertToMaterialCategoryVO(category, parentIdToChildrenMap));
            }
        }
        return rootVOs;
    }
    private MaterialCategoryVO convertToMaterialCategoryVO(Category category, Map<Long, List<Category>> parentIdToChildrenMap) {
        MaterialCategoryVO vo = new MaterialCategoryVO(category.getCategory_id(), category.getCategory_name());
        List<Category> childrenEntities = parentIdToChildrenMap.get(category.getCategory_id());
        if (childrenEntities != null && !childrenEntities.isEmpty()) {
            List<MaterialCategoryVO> childrenVOs = childrenEntities.stream()
                    .map(child -> convertToMaterialCategoryVO(child, parentIdToChildrenMap))
                    .collect(Collectors.toList());
            vo.setChildren(childrenVOs);
        } else {
            vo.setChildren(new ArrayList<>());
        }
        return vo;
    }
    public List<CategoryNodeVO> getCategoryNodeTree() {
        List<Category> allCategories = categoryMapper.findAll();
        Map<Long, List<Category>> parentIdToChildrenMap = allCategories.stream()
                .filter(c -> c.getParent_id() != null)
                .collect(Collectors.groupingBy(Category::getParent_id));
        List<CategoryNodeVO> rootNodes = new ArrayList<>();
        for (Category category : allCategories) {
            if (category.getParent_id() == null) {
                rootNodes.add(convertToCategoryNodeVO(category, parentIdToChildrenMap));
            }
        }
        return rootNodes;
    }
    private CategoryNodeVO convertToCategoryNodeVO(Category category, Map<Long, List<Category>> parentIdToChildrenMap) {
        CategoryNodeVO node = new CategoryNodeVO(category.getCategory_id(), category.getCategory_name());
        List<Category> childrenEntities = parentIdToChildrenMap.get(category.getCategory_id());
        if (childrenEntities != null && !childrenEntities.isEmpty()) {
            List<CategoryNodeVO> childrenNodes = childrenEntities.stream()
                    .map(child -> convertToCategoryNodeVO(child, parentIdToChildrenMap))
                    .collect(Collectors.toList());
            node.setChildren(childrenNodes);
        } else {
            node.setChildren(new ArrayList<>());
        }
        return node;
    }
    @Transactional
    public void handleDragAndDrop(CategoryDragDTO dragDTO) {
        Long draggedNodeId = dragDTO.getDraggedNodeId();
        Long targetParentId = dragDTO.getTargetParentId();
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
        if (targetParentId != null && isDescendant(targetParentId, draggedNodeId)) {
            throw new IllegalArgumentException("不能将父分类拖拽到其子分类下");
        }
        Long originalParentId = draggedNode.getParent_id();
        List<Category> originalSiblings = categoryMapper.findByParentId(originalParentId);
        int currentIdx = 0;
        for (Category sibling : originalSiblings) {
            if (!Objects.equals(sibling.getCategory_id(), draggedNodeId)) {
                if (sibling.getSort_order() != currentIdx) {
                    categoryMapper.updateSortOrder(sibling.getCategory_id(), currentIdx);
                }
                currentIdx++;
            }
        }
        List<Category> targetChildren = categoryMapper.findByParentId(targetParentId);
        for (int i = targetChildren.size() - 1; i >= newIndex; i--) {
            Category childToShift = targetChildren.get(i);
            categoryMapper.updateSortOrder(childToShift.getCategory_id(), i + 1);
        }
        categoryMapper.updateParentAndSortOrder(draggedNodeId, targetParentId, newIndex);
        logger.info("分类 {} 已被拖拽到父分类 {} 的位置 {}", draggedNodeId, targetParentId, newIndex);
    }
    private boolean isDescendant(Long potentialChildId, Long parentId) {
        if (potentialChildId == null || parentId == null) return false;
        Category current = categoryMapper.findById(potentialChildId);
        while (current != null && current.getParent_id() != null) {
            if (current.getParent_id().equals(parentId)) {
                return true;
            }
            current = categoryMapper.findById(current.getParent_id());
        }
        return false;
    }
    @Transactional
    public Category createCategory(String name, Long parentId, Integer sortOrder) {
        if (parentId != null && categoryMapper.findById(parentId) == null) {
            throw new ResourceNotFoundException("父分类ID " + parentId + " 不存在。");
        }
        Category category = new Category();
        category.setCategory_name(name);
        category.setParent_id(parentId);
        if (sortOrder == null) {
            sortOrder = categoryMapper.countChildrenByParentId(parentId);
        }
        category.setSort_order(sortOrder);
        categoryMapper.insertCategory(category);
        return category;
    }
}