package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dto.BatteryStatusDTO;
import com.example.entity.BatteryStatus;
import com.example.mapper.BatteryStatusMapper;
import com.example.service.BatteryStatusService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BatteryStatusServiceImpl implements BatteryStatusService {

    @Resource
    private BatteryStatusMapper batteryStatusMapper;

    @Override
    public void saveBatteryStatus(BatteryStatusDTO batteryStatusDTO) {
        BatteryStatus batteryStatus = new BatteryStatus();
        batteryStatus.setMaterialId(batteryStatusDTO.getMaterialId());
        batteryStatus.setBatteryLevel(batteryStatusDTO.getBatteryLevel());
        batteryStatus.setBatteryHealth(batteryStatusDTO.getBatteryHealth());
        batteryStatus.setRecordTime(LocalDateTime.now());
        batteryStatusMapper.insert(batteryStatus);
    }

    @Override
    public List<BatteryStatus> getBatteryHistory(Integer materialId) {
        QueryWrapper<BatteryStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_id", materialId).orderByDesc("record_time");
        return batteryStatusMapper.selectList(queryWrapper);
    }
}