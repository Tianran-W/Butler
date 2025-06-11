package com.example.vo;

import lombok.Data;
import java.util.Date;

@Data
public class ReturnReminderVO {
    private Integer materialId;
    private String materialName;
    private String borrower;
    private Date dueDate;
}