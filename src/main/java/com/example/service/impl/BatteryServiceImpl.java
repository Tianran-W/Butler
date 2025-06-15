package com.example.service.impl;

import com.example.dto.BatteryCreateDTO;
import com.example.dto.BatteryUpdateDTO;
import com.example.entity.BatteryInfo;
import com.example.entity.Material;
import com.example.mapper.BatteryInfoMapper;
import com.example.mapper.BatteryMapper;
import com.example.mapper.LifeSpanMaterialMapper;
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

    @Resource
    private LifeSpanMaterialMapper materialMapper; // 复用已有的 BaseMapper<Material>

    @Resource
    private BatteryInfoMapper batteryInfoMapper;

    private static final String BATTERY_CATEGORY_NAME = "电池";
    private static final String STATUS_IN_STOCK = "在库可借";
    private static final String STATUS_SCRAPPED = "已报废";

    @Override
    @Transactional
    public BatteryVO createBattery(BatteryCreateDTO createDTO) {
        // 1. 查找 "电池" 分类的ID
        Integer categoryId = batteryMapper.findCategoryIdByName(BATTERY_CATEGORY_NAME);
        if (categoryId == null) {
            throw new IllegalStateException("数据库中未找到 '" + BATTERY_CATEGORY_NAME + "' 分类。");
        }

        // 2. 创建并插入 Material 记录
        Material material = new Material();
        material.setMaterialName(createDTO.getModelName());
        material.setCategoryId(categoryId);
        material.setIsExpensive(createDTO.getIsExpensive());
        material.setSnCode(createDTO.getSnCode());
        material.setQuantity(1); // 电池通常按单个管理
        material.setStatus(STATUS_IN_STOCK);

        materialMapper.insert(material); // 插入后，MyBatis Plus会自动填充 materialId

        // 3. 创建并插入 BatteryInfo 记录
        BatteryInfo batteryInfo = new BatteryInfo();
        batteryInfo.setMaterialId(material.getMaterialId());
        batteryInfo.setLifespanCycles(createDTO.getLifespanCycles());
        batteryInfo.setCurrentCycles(0); // 初始循环次数为0

        batteryInfoMapper.insert(batteryInfo);

        // 4. 查询并返回新创建的电池的完整信息
        return batteryMapper.findBatteryById(material.getMaterialId());
    }

    @Override
    public List<BatteryVO> getAllBatteries() {
        return batteryMapper.findAllBatteries();
    }

    @Override
    public BatteryVO getBatteryById(Integer materialId) {
        return batteryMapper.findBatteryById(materialId);
    }

    @Override
    @Transactional
    public BatteryVO updateBattery(Integer materialId, BatteryUpdateDTO updateDTO) {
        // 如果提供了型号名称，则更新 tb_material 表
        if (updateDTO.getModelName() != null && !updateDTO.getModelName().isEmpty()) {
            Material material = new Material();
            material.setMaterialId(materialId);
            material.setMaterialName(updateDTO.getModelName());
            materialMapper.updateById(material);
        }

        // 如果提供了设计寿命，则更新 tb_battery_info 表
        if (updateDTO.getLifespanCycles() != null) {
            BatteryInfo batteryInfo = new BatteryInfo();
            batteryInfo.setMaterialId(materialId);
            batteryInfo.setLifespanCycles(updateDTO.getLifespanCycles());
            batteryInfoMapper.updateById(batteryInfo);
        }

        return batteryMapper.findBatteryById(materialId);
    }

    @Override
    @Transactional
    public void scrapBattery(Integer materialId) {
        BatteryVO battery = batteryMapper.findBatteryById(materialId);
        if (battery == null) {
            // 在实际应用中，可以抛出一个自定义的 NotFoundException
            throw new RuntimeException("ID为 " + materialId + " 的电池不存在。");
        }
        
        batteryMapper.updateMaterialStatus(materialId, STATUS_SCRAPPED);
    }
}