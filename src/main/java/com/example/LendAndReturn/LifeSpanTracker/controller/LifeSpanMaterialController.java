package com.example.LendAndReturn.LifeSpanTracker.controller;

import com.example.LendAndReturn.LifeSpanTracker.entity.LifeSpanMaterial;
import com.example.LendAndReturn.LifeSpanTracker.service.LifeSpanMaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
public class LifeSpanMaterialController {

    @Resource
    private LifeSpanMaterialService lifeSpanMaterialService;

    @GetMapping("/materials")
    public ResponseEntity<List<LifeSpanMaterial>> getMaterialsByKeyword(@RequestParam String keyword) {
        List<LifeSpanMaterial> materials = lifeSpanMaterialService.getMaterialsByKeyword(keyword);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }
}