package com.example.service;

import com.example.dto.RecommendationRequestDTO;
import com.example.vo.RecommendedMaterialVO;

import java.util.List;

public interface RecommendationService {
    List<RecommendedMaterialVO> getRecommendations(RecommendationRequestDTO requestDTO);
}