package com.example.inventory.category.vo;

import java.util.List;

public class CategoryNodeVO {
    private Long id;
    private String label; // 显示文本
    private List<CategoryNodeVO> children;
    private boolean hasChildren;

    public CategoryNodeVO() {}

    public CategoryNodeVO(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<CategoryNodeVO> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryNodeVO> children) {
        this.children = children;
        this.hasChildren = children != null && !children.isEmpty();
    }
    
    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}