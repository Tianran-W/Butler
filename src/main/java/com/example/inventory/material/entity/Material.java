package com.example.inventory.material.entity;
import java.util.Date;
public class Material {
    private Long material_id;
    private String material_name;
    private Long category_id;
    private Short is_expensive;
    private String sn_code;
    private Integer quantity;
    private Date usage_limit;
    private String status;

    public Long getMaterial_id() {
        return material_id;
    }
    public void setMaterial_id(Long material_id) {
        this.material_id = material_id;
    }
    public String getMaterial_name() {
        return material_name;
    }
    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }
    public Long getCategory_id() {
        return category_id;
    }
    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
    public Short getIs_expensive() {
        return is_expensive;
    }
    public void setIs_expensive(Short is_expensive) {
        this.is_expensive = is_expensive;
    }
    public String getSn_code() {
        return sn_code;
    }
    public void setSn_code(String sn_code) {
        this.sn_code = sn_code;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Date getUsage_limit() {
        return usage_limit;
    }
    public void setUsage_limit(Date usage_limit) {
        this.usage_limit = usage_limit;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}