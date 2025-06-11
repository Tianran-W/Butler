package com.example.service.impl;

import com.example.mapper.AlertMapper;
import com.example.service.AlertService;
import com.example.vo.MaterialAlertVO;
import com.example.vo.ReturnReminderVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    @Resource
    private AlertMapper alertMapper;

    @Override
    public List<MaterialAlertVO> getMaterialAlerts() {
        return alertMapper.findMaterialAlerts();
    }

    @Override
    public List<ReturnReminderVO> getReturnReminders() {
        return alertMapper.findReturnReminders();
    }
}