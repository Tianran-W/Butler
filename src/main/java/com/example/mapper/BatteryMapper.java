package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Battery;
import com.example.vo.BatteryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BatteryMapper extends BaseMapper<Battery> {
    List<BatteryVO> findAllBatteries();
    BatteryVO findVOById(@Param("batteryId") Integer batteryId);
}