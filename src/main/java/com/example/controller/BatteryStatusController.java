package com.example.controller;

import com.example.dto.BatteryStatusDTO;
import com.example.entity.BatteryStatus;
import com.example.service.BatteryStatusService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BatteryStatusController {

    @Resource
    private BatteryStatusService batteryStatusService;

    @PostMapping("/batteryStatus")
    public ResponseEntity<Void> submitBatteryStatus(@RequestBody BatteryStatusDTO batteryStatusDTO) {
        batteryStatusService.saveBatteryStatus(batteryStatusDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/batteryHistory/{batteryId}")
    public ResponseEntity<List<BatteryStatus>> getBatteryHistory(@PathVariable Integer batteryId) {
        List<BatteryStatus> history = batteryStatusService.getBatteryHistory(batteryId);
        return ResponseEntity.ok(history);
    }
}