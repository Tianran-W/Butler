package com.example.controller;

import com.example.entity.Material;
import com.example.service.LifeSpanMaterialService;
import com.example.vo.MaterialVO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LifeSpanMaterialController {

    @Resource
    private LifeSpanMaterialService lifeSpanMaterialService;

    // 获取所有的物资
    @GetMapping("/getAllMaterial")
    public List<MaterialVO> getAllMaterial() {
        return lifeSpanMaterialService.getAllMaterials();
    }
    
    @GetMapping("/material/sn/{snCode}")
    public ResponseEntity<MaterialVO> getMaterialBySnCode(@PathVariable String snCode) {
        MaterialVO material = lifeSpanMaterialService.findBySnCode(snCode);
        if (material != null) {
            return ResponseEntity.ok(material);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}