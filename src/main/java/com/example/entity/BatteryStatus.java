package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_battery_status")
public class BatteryStatus {
    @TableId(type = IdType.AUTO)
    private Integer statusId;
    private Integer materialId;
    private Integer batteryLevel;
    private String batteryHealth;
    private LocalDateTime recordTime;
}