package com.example.LendAndReturn.LifeSpanTracker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_material") // 假设数据库表名还是tb_material，可根据实际情况修改
public class LifeSpanMaterial {
    @TableId(type = IdType.AUTO)
    private Long materialId;
    private String materialName;
    private String status;
    // 使用人、使用项目
    private String peopleUsed;
    private String projectUsed;
}