package com.example.mapper;

import com.example.vo.MaterialAlertVO;
import com.example.vo.ReturnReminderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlertMapper {
    List<MaterialAlertVO> findMaterialAlerts();
    List<ReturnReminderVO> findReturnReminders();
}