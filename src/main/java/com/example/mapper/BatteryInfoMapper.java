package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.BatteryInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatteryInfoMapper extends BaseMapper<BatteryInfo> {
}