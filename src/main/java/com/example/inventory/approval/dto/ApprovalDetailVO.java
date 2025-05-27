package com.example.inventory.approval.dto;

import com.example.inventory.approval.vo.MaterialBriefVO;
import java.util.List;

public class ApprovalDetailVO {
    private String applicant;
    private String department;
    private List<MaterialBriefVO> materials;

    // Getters and Setters
    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<MaterialBriefVO> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialBriefVO> materials) {
        this.materials = materials;
    }
}