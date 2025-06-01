package com.example.controller;

import com.example.dto.ReturnDTO;
import com.example.service.ReturnService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api")
public class ReturnController {

    @Resource
    private ReturnService returnService;

    // 提交归还
    @PostMapping("/return")
    public ResponseEntity<Void> returnMaterial(@Valid @RequestBody ReturnDTO returnDTO) {
        returnService.returnMaterial(returnDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}