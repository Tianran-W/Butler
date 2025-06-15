package com.example.controller;

import com.example.dto.BatteryCreateDTO;
import com.example.dto.BatteryUpdateDTO;
import com.example.service.BatteryService;
import com.example.vo.BatteryVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BatteryController {

    @Resource
    private BatteryService batteryService;

    /**
     * 新增电池
     */
    @PostMapping("/admin/batteries")
    public ResponseEntity<BatteryVO> createBattery(@Valid @RequestBody BatteryCreateDTO createDTO) {
        BatteryVO newBattery = batteryService.createBattery(createDTO);
        return ResponseEntity.ok(newBattery);
    }

    /**
     * 查询所有电池
     */
    @GetMapping("/batteries")
    public ResponseEntity<List<BatteryVO>> getAllBatteries() {
        List<BatteryVO> batteries = batteryService.getAllBatteries();
        return ResponseEntity.ok(batteries);
    }

    /**
     * 查询单个电池详情
     */
    @GetMapping("/batteries/{materialId}")
    public ResponseEntity<BatteryVO> getBatteryById(@PathVariable Integer materialId) {
        BatteryVO battery = batteryService.getBatteryById(materialId);
        if (battery == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(battery);
    }

    /**
     * 更新电池信息
     */
    @PutMapping("/admin/batteries/{materialId}")
    public ResponseEntity<BatteryVO> updateBattery(@PathVariable Integer materialId, @RequestBody BatteryUpdateDTO updateDTO) {
        BatteryVO updatedBattery = batteryService.updateBattery(materialId, updateDTO);
        if (updatedBattery == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBattery);
    }

    /**
     * 删除（报废）电池
     */
    @DeleteMapping("/admin/batteries/{materialId}")
    public ResponseEntity<Void> scrapBattery(@PathVariable Integer materialId) {
        batteryService.scrapBattery(materialId);
        return ResponseEntity.noContent().build();
    }
}