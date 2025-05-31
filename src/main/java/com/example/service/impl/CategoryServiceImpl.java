package com.example.service.impl;

import com.example.entity.Material;
import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.example.service.CategoryService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectAllCategories();
    }

    @Override
    @Transactional
    public void addCategory(String categoryName) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        int rows = categoryMapper.insertCategory(category);
        if (rows == 0) {
            throw new DuplicateKeyException("类别名称已存在");
        }
    }

    @Override
    @Transactional
    public void addNewMaterial(Material material) {
        categoryMapper.insertMaterial(material);
    }
}