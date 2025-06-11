package com.example.controller;

import com.example.service.AlertService;
import com.example.vo.MaterialAlertVO;
import com.example.vo.ReturnReminderVO;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AlertController {

    @Resource
    private AlertService alertService;

    @GetMapping("/admin/materialAlerts")
    public ResponseEntity<List<MaterialAlertVO>> getMaterialAlerts() {
        List<MaterialAlertVO> alerts = alertService.getMaterialAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/returnReminders")
    public ResponseEntity<List<ReturnReminderVO>> getReturnReminders() {
        List<ReturnReminderVO> reminders = alertService.getReturnReminders();
        return ResponseEntity.ok(reminders);
    }
}