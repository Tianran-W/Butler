package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_reimbursement_relation")
public class ReimbursementRelation {
    @TableId(type = IdType.AUTO)
    private Integer relationId;
    private Integer materialId;
    private String reimbursementId;
}