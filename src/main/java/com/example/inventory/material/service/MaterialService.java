package com.example.inventory.material.service;
import com.example.inventory.category.mapper.CategoryMapper;
import com.example.inventory.common.exception.ResourceNotFoundException;
import com.example.inventory.material.dto.MaterialInputDTO;
import com.example.inventory.material.entity.Material;
import com.example.inventory.material.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class MaterialService {
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Transactional
    public Material createMaterial(MaterialInputDTO materialInputDTO) {
        if (categoryMapper.findById(materialInputDTO.getCategory_id()) == null) {
            throw new ResourceNotFoundException("分类ID " + materialInputDTO.getCategory_id() + " 不存在");
        }
        Material material = new Material();
        material.setMaterial_name(materialInputDTO.getMaterial_name());
        material.setCategory_id(materialInputDTO.getCategory_id());
        materialMapper.insertMaterial(material);
        return material;
    }
    public Material getMaterialById(Long id) {
        Material material = materialMapper.findById(id);
        if (material == null) {
            throw new ResourceNotFoundException("物资ID " + id + " 未找到");
        }
        return material;
    }
}