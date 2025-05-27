package com.example.inventory.material.controller;

import com.example.inventory.material.dto.MaterialInputDTO;
import com.example.inventory.material.entity.Material;
import com.example.inventory.material.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/materials") // 假设物资相关接口统一前缀
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping
    public ResponseEntity<Material> createMaterial(@Valid @RequestBody MaterialInputDTO materialInputDTO) {
        Material createdMaterial = materialService.createMaterial(materialInputDTO);
        return new ResponseEntity<>(createdMaterial, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        Material material = materialService.getMaterialById(id);
        return ResponseEntity.ok(material);
    }
}