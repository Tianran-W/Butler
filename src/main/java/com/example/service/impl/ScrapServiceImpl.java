package com.example.service.impl;

import com.example.dto.ReimbursementLinkDTO;
import com.example.dto.ScrapRequestDTO;
import com.example.entity.ReimbursementRelation;
import com.example.mapper.ScrapMapper;
import com.example.service.ScrapService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScrapServiceImpl implements ScrapService {

    @Resource
    private ScrapMapper scrapMapper;

    @Override
    @Transactional
    public void scrapMaterial(ScrapRequestDTO scrapRequestDTO) {
        // This should ideally also create a scrap record, but based on API spec, we update status.
        // Assuming the 'reason' would be logged somewhere, e.g., a new 'tb_scrap_log' table or updating a field.
        // For now, it just updates the material status as a simplified implementation.
        scrapMapper.updateMaterialStatusToScrapped(scrapRequestDTO.getMaterialId(), scrapRequestDTO.getReason());
    }

    @Override
    @Transactional
    public void linkReimbursement(ReimbursementLinkDTO reimbursementLinkDTO) {
        ReimbursementRelation relation = new ReimbursementRelation();
        relation.setMaterialId(reimbursementLinkDTO.getMaterialId());
        relation.setReimbursementId(reimbursementLinkDTO.getReimbursementId());
        scrapMapper.insert(relation);
    }
}