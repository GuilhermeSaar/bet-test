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

    @PostMapping("/save")
    public ResponseEntity<com.gstech.betTest.model.SavedBet> saveBet(
            @RequestBody com.gstech.betTest.model.SavedBet bet) {
        com.gstech.betTest.model.SavedBet saved = scannerService.saveBet(bet);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/save-parlay")
    public ResponseEntity<com.gstech.betTest.model.Parlay> saveParlay(
            @RequestBody com.gstech.betTest.model.Parlay parlay) {
        com.gstech.betTest.model.Parlay saved = scannerService.saveParlay(parlay);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/all")
    public ResponseEntity<java.util.List<com.gstech.betTest.model.SavedBet>> getAllBets() {
        return ResponseEntity.ok(scannerService.getAllSavedBets());
    }

    @GetMapping("/all-parlays")
    public ResponseEntity<java.util.List<com.gstech.betTest.model.Parlay>> getAllParlays() {
        return ResponseEntity.ok(scannerService.getAllParlays());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBet(@PathVariable Long id) {
        scannerService.deleteBet(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-result/{id}")
    public ResponseEntity<com.gstech.betTest.model.SavedBet> updateResult(@PathVariable Long id,
            @RequestBody java.util.Map<String, String> payload) {
        String finalScore = payload.get("finalScore");
        return ResponseEntity.ok(scannerService.updateMatchResult(id, finalScore));
    }
}
