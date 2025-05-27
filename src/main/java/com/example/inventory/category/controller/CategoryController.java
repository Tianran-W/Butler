package com.example.inventory.category.controller;

import com.example.inventory.category.dto.CategoryDragDTO;
import com.example.inventory.category.service.CategoryService;
import com.example.inventory.category.vo.MaterialCategoryVO;
import com.example.inventory.category.vo.CategoryNodeVO; // 引入

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // 用于简单创建请求

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 基础功能模块：获取全部分类树形结构 (MaterialCategoryVO)
    @GetMapping("/tree")
    public ResponseEntity<List<MaterialCategoryVO>> getMaterialCategoryTree() {
        List<MaterialCategoryVO> categoryTree = categoryService.getMaterialCategoryTree();
        return ResponseEntity.ok(categoryTree);
    }
    
    // 分类管理模块：获取用于树形编辑器的节点数据 (CategoryNodeVO)
    @GetMapping("/nodes/tree") // 区分开，或者根据Accept header
    public ResponseEntity<List<CategoryNodeVO>> getCategoryNodeTree() {
        List<CategoryNodeVO> nodeTree = categoryService.getCategoryNodeTree();
        return ResponseEntity.ok(nodeTree);
    }


    // 分类管理模块：处理节点拖拽排序
    @PutMapping("/drag")
    public ResponseEntity<Void> dragCategoryNode(@Valid @RequestBody CategoryDragDTO categoryDragDTO) {
        categoryService.handleDragAndDrop(categoryDragDTO);
        return ResponseEntity.noContent().build(); // 204
    }

    // 示例：新增一个分类 (这个接口不在您的需求中，仅作演示)
    @PostMapping
    public ResponseEntity<com.example.inventory.category.entity.Category> createCategory(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        Long parentId = payload.get("parentId") != null ? Long.valueOf(payload.get("parentId").toString()) : null;
        Integer sortOrder = payload.get("sortOrder") != null ? Integer.valueOf(payload.get("sortOrder").toString()) : null;

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }
        
        com.example.inventory.category.entity.Category newCategory = categoryService.createCategory(name, parentId, sortOrder);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }
}   