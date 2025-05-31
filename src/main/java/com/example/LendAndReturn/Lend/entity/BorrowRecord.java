package com.example.LendAndReturn.Lend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("borrow_record")
public class BorrowRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long materialId;
    private String materialName;
    private String type;
    private String snCode;
    private String category;
    private Integer currentStock;
    private Boolean isPrecious;
    private Long userId;
    private String userName;
    private LocalDateTime borrowTime;
    private String approvalStatus; // 待审批/通过/拒绝/已归还
    private LocalDateTime dueDate;
    private Boolean isOverdue;
    private String approver;       // 审批人
    private LocalDateTime approvalTime; // 审批时间
    private String projectUsed;    // 用于哪个项目

}