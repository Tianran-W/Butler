package com.example.service;

import com.example.entity.Material;
import com.example.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void addCategory(String categoryName);

    void addNewMaterial(Material material);
}