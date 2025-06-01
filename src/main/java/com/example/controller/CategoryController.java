package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.entity.Category;
import com.example.entity.Material;
import com.example.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    // 获取物资分布
    @GetMapping("/materialsCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // 新建物资类别
    @PostMapping("/materialsNewCategories")
    public ResponseEntity<Void> addCategory(@RequestBody CategoryDTO categoryDTO) {
        String categoryName = categoryDTO.getCategoryName();
        categoryService.addCategory(categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 物资入库
    @PostMapping("/addNewMaterials")
    public ResponseEntity<Void> addNewMaterial(@RequestBody Material material) {
        categoryService.addNewMaterial(material);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}