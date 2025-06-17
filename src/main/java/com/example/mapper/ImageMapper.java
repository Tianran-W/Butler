package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ImageMapper extends BaseMapper<Image> {
    List<Image> findByRecordTypeAndRecordId(@Param("recordType") String recordType, @Param("recordId") String recordId);
}