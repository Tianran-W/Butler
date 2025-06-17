package com.example.service.impl;

import com.example.dto.BatteryCreateDTO;
import com.example.dto.BatteryUpdateDTO;
import com.example.entity.Battery;
import com.example.mapper.BatteryMapper;
import com.example.service.BatteryService;
import com.example.vo.BatteryVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BatteryServiceImpl implements BatteryService {

    @Resource
    private BatteryMapper batteryMapper;

    private static final String STATUS_IN_STOCK = "在库可借";
    private static final String STATUS_SCRAPPED = "已报废";

    @Override
    @Transactional
    public BatteryVO createBattery(BatteryCreateDTO createDTO) {
        Battery battery = new Battery();
        battery.setModelName(createDTO.getModelName());
        battery.setSnCode(createDTO.getSnCode());
        battery.setLifespanCycles(createDTO.getLifespanCycles());
        battery.setCurrentCycles(0);
        battery.setStatus(STATUS_IN_STOCK);

        batteryMapper.insert(battery);

        return batteryMapper.findVOById(battery.getBatteryId());
    }

    @Override
    public List<BatteryVO> getAllBatteries() {
        return batteryMapper.findAllBatteries();
    }

    @Override
    public BatteryVO getBatteryById(Integer batteryId) {
        return batteryMapper.findVOById(batteryId);
    }

    @Override
    @Transactional
    public BatteryVO updateBattery(Integer batteryId, BatteryUpdateDTO updateDTO) {
        Battery battery = batteryMapper.selectById(batteryId);
        if (battery == null) {
            return null;
        }

        if (updateDTO.getModelName() != null && !updateDTO.getModelName().isEmpty()) {
            battery.setModelName(updateDTO.getModelName());
        }

        if (updateDTO.getLifespanCycles() != null) {
            battery.setLifespanCycles(updateDTO.getLifespanCycles());
        }
        batteryMapper.updateById(battery);
        return batteryMapper.findVOById(batteryId);
    }

    @Override
    @Transactional
    public void scrapBattery(Integer batteryId) {
        Battery battery = batteryMapper.selectById(batteryId);
        if (battery == null) {
            throw new RuntimeException("ID为 " + batteryId + " 的电池不存在。");
        }
        battery.setStatus(STATUS_SCRAPPED);
        batteryMapper.updateById(battery);
    }
}