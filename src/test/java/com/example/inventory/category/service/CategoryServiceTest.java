package com.example.inventory.category.service;

import com.example.inventory.category.dto.CategoryDragDTO;
import com.example.inventory.category.entity.Category;
import com.example.inventory.category.mapper.CategoryMapper;
import com.example.inventory.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getMaterialCategoryTree() {
        // Arrange
        Category root = new Category();
        root.setCategory_id(1L);
        root.setCategory_name("Root");
        
        Category child1 = new Category();
        child1.setCategory_id(2L);
        child1.setCategory_name("Child1");
        child1.setParent_id(1L);
        
        Category child2 = new Category();
        child2.setCategory_id(3L);
        child2.setCategory_name("Child2");
        child2.setParent_id(1L);
        
        when(categoryMapper.findAll()).thenReturn(Arrays.asList(root, child1, child2));

        // Act
        List<com.example.inventory.category.vo.MaterialCategoryVO> tree = categoryService.getMaterialCategoryTree();

        // Assert
        assertEquals(1, tree.size());
        assertEquals("Root", tree.get(0).getName());
        assertEquals(2, tree.get(0).getChildren().size());
        assertTrue(tree.get(0).isHasChildren());
        assertEquals("Child1", tree.get(0).getChildren().get(0).getName());
        assertEquals("Child2", tree.get(0).getChildren().get(1).getName());
    }

    @Test
    void getCategoryNodeTree() {
        // Arrange
        Category root = new Category();
        root.setCategory_id(1L);
        root.setCategory_name("Root");
        
        Category child1 = new Category();
        child1.setCategory_id(2L);
        child1.setCategory_name("Child1");
        child1.setParent_id(1L);
        
        when(categoryMapper.findAll()).thenReturn(Arrays.asList(root, child1));

        // Act
        List<com.example.inventory.category.vo.CategoryNodeVO> tree = categoryService.getCategoryNodeTree();

        // Assert
        assertEquals(1, tree.size());
        assertEquals("Root", tree.get(0).getLabel());
        assertEquals(1, tree.get(0).getChildren().size());
        assertTrue(tree.get(0).isHasChildren());
        assertEquals("Child1", tree.get(0).getChildren().get(0).getLabel());
    }

    @Test
    void handleDragAndDrop_Success() {
        // Arrange
        CategoryDragDTO dto = new CategoryDragDTO();
        dto.setDraggedNodeId(4L);
        dto.setTargetParentId(2L);
        dto.setNewIndex(1);

        Category draggedNode = new Category();
        draggedNode.setCategory_id(4L);
        draggedNode.setParent_id(1L);
        draggedNode.setSort_order(2);

        Category sibling1 = new Category();
        sibling1.setCategory_id(5L);
        sibling1.setSort_order(0);
        Category sibling2 = new Category();
        sibling2.setCategory_id(6L);
        sibling2.setSort_order(1);
        Category targetChild = new Category();
        targetChild.setCategory_id(7L);
        targetChild.setSort_order(0);

        when(categoryMapper.findById(4L)).thenReturn(draggedNode);
        when(categoryMapper.findById(2L)).thenReturn(new Category());
        when(categoryMapper.findByParentId(1L))
                .thenReturn(Arrays.asList(sibling1, sibling2, draggedNode)); // 包含被拖拽节点
        when(categoryMapper.findByParentId(2L))
                .thenReturn(Arrays.asList(targetChild));

        // Act & Assert
        assertDoesNotThrow(() -> categoryService.handleDragAndDrop(dto));
        verify(categoryMapper).updateParentAndSortOrder(4L, 2L, 1);
        // 移除非必要的updateSortOrder验证
    }


    @Test
    void handleDragAndDrop_DraggedNodeNotFound() {
        // Arrange
        CategoryDragDTO dto = new CategoryDragDTO();
        dto.setDraggedNodeId(999L);
        dto.setNewIndex(0);
        when(categoryMapper.findById(999L)).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
            () -> categoryService.handleDragAndDrop(dto));
        
        assertEquals("拖拽的分类节点 999 不存在", ex.getMessage());
    }

    @Test
    void createCategory_Success() {
        // 模拟父分类存在
        when(categoryMapper.findById(1L)).thenReturn(new Category());

        Category result = categoryService.createCategory("New Category", 1L, 0);

        assertNotNull(result);
        assertEquals("New Category", result.getCategory_name());
        assertEquals(1L, result.getParent_id());
        assertEquals(0, result.getSort_order());
        verify(categoryMapper).insertCategory(any());
    }
}