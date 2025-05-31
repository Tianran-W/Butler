package com.example.LendAndReturn.Return.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.LendAndReturn.Return.entity.ReturnRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReturnRecordMapper extends BaseMapper<ReturnRecord> {
}