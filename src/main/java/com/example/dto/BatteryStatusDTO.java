package com.example.dto;

import lombok.Data;

@Data
public class BatteryStatusDTO {
    private Integer batteryId;
    private Integer batteryLevel;
    private String batteryHealth;
}