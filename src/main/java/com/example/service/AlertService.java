package com.example.service;

import com.example.vo.MaterialAlertVO;
import com.example.vo.ReturnReminderVO;

import java.util.List;

public interface AlertService {
    List<MaterialAlertVO> getMaterialAlerts();
    List<ReturnReminderVO> getReturnReminders();
}