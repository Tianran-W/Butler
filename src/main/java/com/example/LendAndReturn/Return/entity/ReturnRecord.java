package com.example.LendAndReturn.Return.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("return_record")
public class ReturnRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    // 关联借用记录ID
    @TableField("borrow_record_id")
    private Long borrowRecordId;
    // 物资ID
    @TableField("material_id")
    private Long materialId;
    // 归还人ID
    @TableField("user_id")
    private Long userId;
    // 归还时间
    @TableField("return_time")
    private LocalDateTime returnTime;
    // 归还状态（如：正常归还、损坏等）
    @TableField("return_status")
    private String returnStatus;
    // 归还照片URL（存储路径）
    @TableField("photo_url")
    private String photoUrl;

    // 扩展字段，从借用记录关联获取物资名称等信息
    @TableField(exist = false)
    private String materialName;
    @TableField(exist = false)
    private LocalDateTime borrowTime;
}