package com.example.service.impl;

import com.example.dto.ReturnDTO;
import com.example.mapper.ReturnMapper;
import com.example.service.ReturnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

@Service
public class ReturnServiceImpl implements ReturnService {

    @Resource
    private ReturnMapper returnMapper;

    @Override
    @Transactional
    public void returnMaterial(ReturnDTO returnDTO) {
        returnMapper.updateReturnTime(returnDTO.getMaterial_id());
        returnMapper.updateMaterialStatus(returnDTO.getMaterial_id());
    }
}