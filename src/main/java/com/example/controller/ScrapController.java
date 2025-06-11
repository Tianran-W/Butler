package com.example.controller;

import com.example.dto.ReimbursementLinkDTO;
import com.example.dto.ScrapRequestDTO;
import com.example.service.ScrapService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ScrapController {

    @Resource
    private ScrapService scrapService;

    @PostMapping("/material/scrap")
    public ResponseEntity<Void> scrapMaterial(@RequestBody ScrapRequestDTO scrapRequestDTO) {
        scrapService.scrapMaterial(scrapRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/reimbursement/link")
    public ResponseEntity<Void> linkReimbursement(@RequestBody ReimbursementLinkDTO reimbursementLinkDTO) {
        scrapService.linkReimbursement(reimbursementLinkDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}