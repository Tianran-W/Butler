package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Material;
import com.example.mapper.LifeSpanMaterialMapper;
import com.example.service.LifeSpanMaterialService;
import com.example.vo.MaterialVO;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;

@Service
public class LifeSpanMaterialServiceImpl extends ServiceImpl<LifeSpanMaterialMapper, Material> implements LifeSpanMaterialService {
    @Resource
    private LifeSpanMaterialMapper lifeSpanMaterialMapper;

    @Override
    public List<MaterialVO> getAllMaterials() {
        return lifeSpanMaterialMapper.getAllMaterials();
    }

    @Override
    public MaterialVO findBySnCode(String snCode) {
        return lifeSpanMaterialMapper.findBySnCode(snCode);
    }
}