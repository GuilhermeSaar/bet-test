package com.gstech.betTest.controller;

import com.gstech.betTest.dto.ScannerRequest;
import com.gstech.betTest.dto.ScannerResponse;
import com.gstech.betTest.service.ScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scanner")
@CrossOrigin(origins = "*") // Allow frontend to call
public class ScannerController {

    @Autowired
    private ScannerService scannerService;

    @PostMapping("/analyze")
    public ResponseEntity<ScannerResponse> analyzeMatch(@RequestBody ScannerRequest request) {
        ScannerResponse response = scannerService.analyzeMatch(request);
        return ResponseEntity.ok(response);
    }
}
