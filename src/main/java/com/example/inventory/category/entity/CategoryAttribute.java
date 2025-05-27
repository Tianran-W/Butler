package com.example.inventory.category.entity;

public class CategoryAttribute {
    private Long id; // 自增主键
    private Long categoryId; // 关联的分类ID
    private String attributeName; // 属性名，例如 "CPU型号", "内存大小"
    private String attributeKey;  // 属性键，例如 "cpu_model", "memory_size"，用于存储到specifications中
    private String attributeType; // 属性类型，例如 "STRING", "NUMBER", "BOOLEAN", "SELECT"
    private String options; // 如果类型是SELECT，这里存储选项（如JSON字符串）
    private boolean isRequired; // 是否必填
    private Integer sortOrder; // 属性在分类下的显示顺序

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        this.isRequired = required;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}