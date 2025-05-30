package com.example.inventory.category.entity;
public class Category {
    private Long category_id;
    private String category_name;
    private Long parent_id;
    private Integer sort_order;

    public Long getCategory_id() {
        return category_id;
    }
    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
    public String getCategory_name() {
        return category_name;
    }
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
    public Long getParent_id() {
        return parent_id;
    }
    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }
    public Integer getSort_order() {
        return sort_order;
    }
    public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }
}