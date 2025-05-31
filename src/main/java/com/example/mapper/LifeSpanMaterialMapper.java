package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Material;
import com.example.vo.MaterialVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LifeSpanMaterialMapper extends BaseMapper<Material> {
    List<MaterialVO> getAllMaterials();
}