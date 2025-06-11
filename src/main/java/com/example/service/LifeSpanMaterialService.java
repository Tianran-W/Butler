package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Material;
import com.example.vo.MaterialVO;
import java.util.List;

public interface LifeSpanMaterialService extends IService<Material> {
    List<MaterialVO> getAllMaterials();
    MaterialVO findBySnCode(String snCode);
}