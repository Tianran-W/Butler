package com.example.dto;

import lombok.Data;

@Data
public class BatteryStatusDTO {
    private Integer materialId;
    private Integer batteryLevel;
    private String batteryHealth;
}