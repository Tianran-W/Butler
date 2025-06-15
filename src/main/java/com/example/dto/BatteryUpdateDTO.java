package com.example.dto;

import lombok.Data;

@Data
public class BatteryUpdateDTO {
    private String modelName;
    private Integer lifespanCycles;
}