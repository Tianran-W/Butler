package com.example.inventory.material.service;

import com.example.inventory.category.mapper.CategoryMapper;
import com.example.inventory.material.dto.MaterialInputDTO;
import com.example.inventory.material.entity.Material;
import com.example.inventory.material.mapper.MaterialMapper;
import com.example.inventory.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private MaterialMapper materialMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private MaterialService materialService;

    @Test
    void createMaterial_Success() {
        // Arrange
        MaterialInputDTO inputDTO = new MaterialInputDTO();
        inputDTO.setMaterial_name("Laptop");
        inputDTO.setCategory_id(1L);
        
        when(categoryMapper.findById(1L)).thenReturn(new com.example.inventory.category.entity.Category());

        // Act
        Material result = materialService.createMaterial(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Laptop", result.getMaterial_name());
        assertEquals(1L, result.getCategory_id().longValue());
        verify(materialMapper).insertMaterial(any());
    }

    @Test
    void createMaterial_CategoryNotFound() {
        // Arrange
        MaterialInputDTO inputDTO = new MaterialInputDTO();
        inputDTO.setMaterial_name("Laptop");
        inputDTO.setCategory_id(999L);
        
        when(categoryMapper.findById(999L)).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
            () -> materialService.createMaterial(inputDTO));
        
        assertEquals("分类ID 999 不存在", ex.getMessage());
    }

    @Test
    void getMaterialById_Success() {
        // Arrange
        Material expected = new Material();
        expected.setMaterial_id(1L);
        expected.setMaterial_name("Laptop");
        
        when(materialMapper.findById(1L)).thenReturn(expected);

        // Act
        Material result = materialService.getMaterialById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getMaterial_id().longValue());
        assertEquals("Laptop", result.getMaterial_name());
    }

    @Test
    void getMaterialById_NotFound() {
        // Arrange
        when(materialMapper.findById(999L)).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
            () -> materialService.getMaterialById(999L));
        
        assertEquals("物资ID 999 未找到", ex.getMessage());
    }
}