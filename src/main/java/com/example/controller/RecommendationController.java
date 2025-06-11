package com.example.controller;

import com.example.dto.RecommendationRequestDTO;
import com.example.service.RecommendationService;
import com.example.vo.RecommendedMaterialVO;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecommendationController {

    @Resource
    private RecommendationService recommendationService;

    @PostMapping("/recommendMaterials")
    public ResponseEntity<List<RecommendedMaterialVO>> getRecommendedMaterials(@RequestBody RecommendationRequestDTO requestDTO) {
        List<RecommendedMaterialVO> recommendations = recommendationService.getRecommendations(requestDTO);
        return ResponseEntity.ok(recommendations);
    }
}