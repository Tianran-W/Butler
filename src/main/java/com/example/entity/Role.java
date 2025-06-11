package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer roleId;
    private String roleName;
    private String permissions;
}