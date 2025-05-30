package com.example.inventory.material.entity;

import java.util.Map;
// import com.fasterxml.jackson.databind.ObjectMapper; // 如果需要手动序列化
// import com.fasterxml.jackson.core.type.TypeReference; // 如果需要手动序列化

public class Material {
    private Long id;
    private String materialName;
    private Long categoryId;
    private String specificationsJson; // 存储JSON字符串
    private transient Map<String, Object> specificationsMap; // 瞬态字段，不直接持久化
    private String description;

    // private static final ObjectMapper objectMapper = new ObjectMapper(); // 如果需要手动序列化

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSpecificationsJson() {
        return specificationsJson;
    }

    public void setSpecificationsJson(String specificationsJson) {
        this.specificationsJson = specificationsJson;
        // 如果使用手动序列化/反序列化
        // try {
        //     if (specificationsJson != null && !specificationsJson.isEmpty()) {
        //         this.specificationsMap = objectMapper.readValue(specificationsJson, new TypeReference<Map<String, Object>>() {});
        //     } else {
        //         this.specificationsMap = null;
        //     }
        // } catch (Exception e) {
        //     // 处理反序列化错误
        //     this.specificationsMap = null;
        // }
    }

    public Map<String, Object> getSpecificationsMap() {
        return specificationsMap;
    }

    public void setSpecificationsMap(Map<String, Object> specificationsMap) {
        this.specificationsMap = specificationsMap;
        // 如果使用手动序列化/反序列化
        // try {
        //     if (specificationsMap != null && !specificationsMap.isEmpty()) {
        //         this.specificationsJson = objectMapper.writeValueAsString(specificationsMap);
        //     } else {
        //         this.specificationsJson = null;
        //     }
        // } catch (Exception e) {
        //     // 处理序列化错误
        //     this.specificationsJson = null;
        // }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}