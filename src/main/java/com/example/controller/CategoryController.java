package com.example.controller;

import com.example.dto.CategoryDTO;
import com.example.entity.Category;
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

    @GetMapping("/materialsCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/materialsNewCategories")
    public ResponseEntity<Void> addCategory(@RequestBody CategoryDTO categoryDTO) {
        String categoryName = categoryDTO.getCategoryName();
        categoryService.addCategory(categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}