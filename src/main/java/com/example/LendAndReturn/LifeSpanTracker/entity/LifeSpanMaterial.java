package com.example.LendAndReturn.LifeSpanTracker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_material")
public class LifeSpanMaterial {
    @TableId(type = IdType.AUTO)
    private Long materialId;
    private String materialName;
    private String status;

    // 新增字段：当前使用人ID和名称
    @TableField(exist = false)
    private Long currentUserId;

    @TableField(exist = false)
    private String currentUserName;

    // 新增字段：当前使用项目
    @TableField(exist = false)
    private String currentProject;

    // 新增字段：最新借用时间
    @TableField(exist = false)
    private java.time.LocalDateTime latestBorrowTime;
}