package com.example.service;

import com.example.dto.BatteryStatusDTO;
import com.example.entity.BatteryStatus;

import java.util.List;

public interface BatteryStatusService {
    void saveBatteryStatus(BatteryStatusDTO batteryStatusDTO);
    List<BatteryStatus> getBatteryHistory(Integer materialId);
}