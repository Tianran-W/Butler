package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_battery")
public class Battery {
    @TableId(type = IdType.AUTO)
    private Integer batteryId;
    private String modelName;
    private String snCode;
    private String status;
    private Integer lifespanCycles;
    private Integer currentCycles;
}