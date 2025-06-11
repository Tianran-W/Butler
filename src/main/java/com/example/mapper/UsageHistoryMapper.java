package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface UsageHistoryMapper {
    List<Map<String, Object>> getUsageHistory();
}