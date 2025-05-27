package com.example.inventory.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CategoryDragDTO {
    @NotNull(message = "拖拽节点ID不能为空")
    private Long draggedNodeId;

    // targetParentId 可以为 null，表示拖拽到根级别
    private Long targetParentId; 

    @NotNull(message = "新索引不能为空")
    @Min(value = 0, message = "索引不能为负")
    private Integer newIndex;

    // Getters and Setters
    public Long getDraggedNodeId() {
        return draggedNodeId;
    }

    public void setDraggedNodeId(Long draggedNodeId) {
        this.draggedNodeId = draggedNodeId;
    }

    public Long getTargetParentId() {
        return targetParentId;
    }

    public void setTargetParentId(Long targetParentId) {
        this.targetParentId = targetParentId;
    }

    public Integer getNewIndex() {
        return newIndex;
    }

    public void setNewIndex(Integer newIndex) {
        this.newIndex = newIndex;
    }
}