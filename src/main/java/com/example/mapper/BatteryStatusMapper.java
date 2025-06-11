package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.BatteryStatus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatteryStatusMapper extends BaseMapper<BatteryStatus> {
}