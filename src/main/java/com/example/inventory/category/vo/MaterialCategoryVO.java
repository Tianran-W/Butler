package com.example.inventory.category.vo;

import java.util.List;

public class MaterialCategoryVO {
    private Long id;
    private String name;
    private List<MaterialCategoryVO> children;
    private boolean hasChildren; // 方便前端判断

    public MaterialCategoryVO() {}

    public MaterialCategoryVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MaterialCategoryVO> getChildren() {
        return children;
    }

    public void setChildren(List<MaterialCategoryVO> children) {
        this.children = children;
        this.hasChildren = children != null && !children.isEmpty();
    }

    public boolean isHasChildren() {
        return hasChildren;
    }
    
    // setHasChildren 通常由 setChildren 内部逻辑决定，但如果需要外部设置也可以提供
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}