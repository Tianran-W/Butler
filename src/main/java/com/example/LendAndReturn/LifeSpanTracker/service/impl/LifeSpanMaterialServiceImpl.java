package com.example.LendAndReturn.LifeSpanTracker.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.LendAndReturn.LifeSpanTracker.entity.LifeSpanMaterial;
import com.example.LendAndReturn.LifeSpanTracker.mapper.LifeSpanMaterialMapper;
import com.example.LendAndReturn.LifeSpanTracker.service.LifeSpanMaterialService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class LifeSpanMaterialServiceImpl extends ServiceImpl<LifeSpanMaterialMapper, LifeSpanMaterial> implements LifeSpanMaterialService {

    @Resource
    private LifeSpanMaterialMapper lifeSpanMaterialMapper;

    @Override
    public List<LifeSpanMaterial> getMaterialsByKeyword(String keyword) {
        return lifeSpanMaterialMapper.selectByKeyword(keyword);
    }

    @Override
    public List<LifeSpanMaterial> getAllTable(){
        return lifeSpanMaterialMapper.getAll();
    }
}