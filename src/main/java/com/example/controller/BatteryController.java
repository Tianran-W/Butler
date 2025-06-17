package com.example.controller;

import com.example.dto.BatteryCreateDTO;
import com.example.dto.BatteryUpdateDTO;
import com.example.service.BatteryService;
import com.example.vo.BatteryVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BatteryController {

    @Resource
    private BatteryService batteryService;

    @PostMapping("/admin/batteries")
    public ResponseEntity<BatteryVO> createBattery(@Valid @RequestBody BatteryCreateDTO createDTO) {
        BatteryVO newBattery = batteryService.createBattery(createDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{batteryId}")
                .buildAndExpand(newBattery.getBatteryId())
                .toUri();
        return ResponseEntity.created(location).body(newBattery);
    }

    @GetMapping("/batteries")
    public ResponseEntity<List<BatteryVO>> getAllBatteries() {
        List<BatteryVO> batteries = batteryService.getAllBatteries();
        return ResponseEntity.ok(batteries);
    }

    @GetMapping("/batteries/{batteryId}")
    public ResponseEntity<BatteryVO> getBatteryById(@PathVariable Integer batteryId) {
        BatteryVO battery = batteryService.getBatteryById(batteryId);
        if (battery == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(battery);
    }

    @PutMapping("/admin/batteries/{batteryId}")
    public ResponseEntity<BatteryVO> updateBattery(@PathVariable Integer batteryId, @RequestBody BatteryUpdateDTO updateDTO) {
        BatteryVO updatedBattery = batteryService.updateBattery(batteryId, updateDTO);
        if (updatedBattery == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBattery);
    }

    @DeleteMapping("/admin/batteries/{batteryId}")
    public ResponseEntity<Void> scrapBattery(@PathVariable Integer batteryId) {
        batteryService.scrapBattery(batteryId);
        return ResponseEntity.noContent().build();
    }
}