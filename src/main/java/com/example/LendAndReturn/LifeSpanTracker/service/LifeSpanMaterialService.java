package com.example.LendAndReturn.LifeSpanTracker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.LendAndReturn.LifeSpanTracker.entity.LifeSpanMaterial;

import java.util.List;

public interface LifeSpanMaterialService extends IService<LifeSpanMaterial> {
    List<LifeSpanMaterial> getMaterialsByKeyword(String keyword);
}