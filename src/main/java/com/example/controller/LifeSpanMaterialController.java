package com.example.controller;

import com.example.entity.Material;
import com.example.service.LifeSpanMaterialService;
import com.example.vo.MaterialVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LifeSpanMaterialController {

    @Resource
    private LifeSpanMaterialService lifeSpanMaterialService;

    @GetMapping("/getAllMaterial")
    public List<MaterialVO> getAllMaterial() {
        return lifeSpanMaterialService.getAllMaterials();
    }
}