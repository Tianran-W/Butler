package com.example.dto;

import lombok.Data;

@Data
public class RecommendationRequestDTO {
    private String projectType;
    private Integer participantCount;
}