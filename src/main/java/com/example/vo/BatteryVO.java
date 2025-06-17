package com.example.vo;

import lombok.Data;

@Data
public class BatteryVO {
    private Integer batteryId;
    private String modelName;
    private String snCode;
    private String status;
    private Integer lifespanCycles;
    private Integer currentCycles;
}