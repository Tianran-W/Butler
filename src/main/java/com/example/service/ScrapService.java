package com.example.service;

import com.example.dto.ReimbursementLinkDTO;
import com.example.dto.ScrapRequestDTO;

public interface ScrapService {
    void scrapMaterial(ScrapRequestDTO scrapRequestDTO);
    void linkReimbursement(ReimbursementLinkDTO reimbursementLinkDTO);
}