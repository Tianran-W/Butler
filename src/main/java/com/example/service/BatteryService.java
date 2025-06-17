package com.example.service;

import com.example.dto.BatteryCreateDTO;
import com.example.dto.BatteryUpdateDTO;
import com.example.vo.BatteryVO;

import java.util.List;

public interface BatteryService {
    BatteryVO createBattery(BatteryCreateDTO createDTO);
    List<BatteryVO> getAllBatteries();
    BatteryVO getBatteryById(Integer batteryId);
    BatteryVO updateBattery(Integer batteryId, BatteryUpdateDTO updateDTO);
    void scrapBattery(Integer batteryId);
}