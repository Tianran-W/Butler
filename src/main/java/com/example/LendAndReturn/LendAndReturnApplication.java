package com.example.LendAndReturn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.LendAndReturn.**.mapper") // 扫描MyBatis Mapper接口
public class LendAndReturnApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.example.LendAndReturn.LendAndReturnApplication.class, args);
    }

}
