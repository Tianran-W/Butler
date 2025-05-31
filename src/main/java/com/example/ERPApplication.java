package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.LendAndReturn.Lend.mapper")
@MapperScan("com.example.LendAndReturn.Return.mapper")
@MapperScan("com.example.LendAndReturn.LifeSpanTracker.mapper")
@MapperScan("com.example.inventory.approval.mapper")
@MapperScan("com.example.inventory.category.mapper")
@MapperScan("com.example.inventory.material.mapper")

public class ERPApplication {
    public static void main(String[] args) {
        SpringApplication.run(ERPApplication.class, args);
    }
}
