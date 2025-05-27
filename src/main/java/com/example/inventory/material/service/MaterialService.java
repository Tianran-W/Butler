package com.example.inventory.material.service;

import com.example.inventory.category.mapper.CategoryMapper;
import com.example.inventory.common.exception.ResourceNotFoundException;
import com.example.inventory.material.dto.MaterialInputDTO;
import com.example.inventory.material.entity.Material;
import com.example.inventory.material.mapper.MaterialMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private CategoryMapper categoryMapper; // 用于校验categoryId是否存在

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public Material createMaterial(MaterialInputDTO materialInputDTO) {
        // 校验分类ID是否存在
        if (categoryMapper.findById(materialInputDTO.getCategoryId()) == null) {
            throw new ResourceNotFoundException("分类ID " + materialInputDTO.getCategoryId() + " 不存在");
        }

        Material material = new Material();
        material.setMaterialName(materialInputDTO.getMaterialName());
        material.setCategoryId(materialInputDTO.getCategoryId());
        material.setDescription(materialInputDTO.getDescription());

        if (materialInputDTO.getSpecifications() != null && !materialInputDTO.getSpecifications().isEmpty()) {
            try {
                material.setSpecificationsJson(objectMapper.writeValueAsString(materialInputDTO.getSpecifications()));
            } catch (JsonProcessingException e) {
                // 处理序列化错误，可以抛出自定义异常或记录日志
                throw new RuntimeException("物资规格序列化失败", e);
            }
        }
        
        materialMapper.insertMaterial(material); // 假设insertMaterial会返回自增ID并设置到material对象中
        return material;
    }

    public Material getMaterialById(Long id) {
        Material material = materialMapper.findById(id);
        if (material == null) {
            throw new ResourceNotFoundException("物资ID " + id + " 未找到");
        }
        // 如果需要，这里可以反序列化 specificationsJson 到 specificationsMap
        // 例如，通过 material.getSpecificationsMap() (如果getter中包含反序列化逻辑)
        // 或者手动：
        // if (material.getSpecificationsJson() != null && !material.getSpecificationsJson().isEmpty()) {
        //     try {
        //         material.setSpecificationsMap(objectMapper.readValue(material.getSpecificationsJson(), new TypeReference<Map<String, Object>>() {}));
        //     } catch (JsonProcessingException e) {
        //          throw new RuntimeException("物资规格反序列化失败", e);
        //     }
        // }
        return material;
    }
}