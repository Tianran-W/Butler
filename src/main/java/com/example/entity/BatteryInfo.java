package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_battery_info")
public class BatteryInfo {
    @TableId
    private Integer materialId;
    private Integer lifespanCycles;
    private Integer currentCycles;
}