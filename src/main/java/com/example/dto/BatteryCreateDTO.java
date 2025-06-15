package com.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class BatteryCreateDTO {
    @NotEmpty(message = "型号名称不能为空")
    private String modelName;

    @NotEmpty(message = "SN码不能为空")
    private String snCode;

    @NotNull(message = "设计寿命不能为空")
    @PositiveOrZero(message = "设计寿命不能为负数")
    private Integer lifespanCycles;

    @NotNull(message = "是否贵重不能为空")
    private Integer isExpensive;
}