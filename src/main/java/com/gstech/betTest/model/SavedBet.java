package com.gstech.betTest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_saved_bets")
public class SavedBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String homeTeam;
    private String awayTeam;

    private double probabilityOver15;
    private double fairOddOver15;
    private double marketOdd;

    // xG (Feitos/Sofridos)
    private double homeAvgScored;
    private double homeAvgConceded;
    private double awayAvgScored;
    private double awayAvgConceded;

    private boolean isValueBet;
    private String confidenceLevel;

    private String finalScore; // ex: "2-1"
    private String resultOutcome = "PENDENTE"; // WIN, LOSS, PENDING

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public SavedBet() {
    }

    public SavedBet(String homeTeam, String awayTeam, double probabilityOver15, double fairOddOver15, double marketOdd,
            double homeAvgScored, double homeAvgConceded, double awayAvgScored, double awayAvgConceded,
            boolean isValueBet, String confidenceLevel) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.probabilityOver15 = probabilityOver15;
        this.fairOddOver15 = fairOddOver15;
        this.marketOdd = marketOdd;
        this.homeAvgScored = homeAvgScored;
        this.homeAvgConceded = homeAvgConceded;
        this.awayAvgScored = awayAvgScored;
        this.awayAvgConceded = awayAvgConceded;
        this.isValueBet = isValueBet;
        this.confidenceLevel = confidenceLevel;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public double getProbabilityOver15() {
        return probabilityOver15;
    }

    public void setProbabilityOver15(double probabilityOver15) {
        this.probabilityOver15 = probabilityOver15;
    }

    public double getFairOddOver15() {
        return fairOddOver15;
    }

    public void setFairOddOver15(double fairOddOver15) {
        this.fairOddOver15 = fairOddOver15;
    }

    public double getMarketOdd() {
        return marketOdd;
    }

    public void setMarketOdd(double marketOdd) {
        this.marketOdd = marketOdd;
    }

    public double getHomeAvgScored() {
        return homeAvgScored;
    }

    public void setHomeAvgScored(double homeAvgScored) {
        this.homeAvgScored = homeAvgScored;
    }

    public double getHomeAvgConceded() {
        return homeAvgConceded;
    }

    public void setHomeAvgConceded(double homeAvgConceded) {
        this.homeAvgConceded = homeAvgConceded;
    }

    public double getAwayAvgScored() {
        return awayAvgScored;
    }

    public void setAwayAvgScored(double awayAvgScored) {
        this.awayAvgScored = awayAvgScored;
    }

    public double getAwayAvgConceded() {
        return awayAvgConceded;
    }

    public void setAwayAvgConceded(double awayAvgConceded) {
        this.awayAvgConceded = awayAvgConceded;
    }

    public boolean isValueBet() {
        return isValueBet;
    }

    public void setValueBet(boolean valueBet) {
        isValueBet = valueBet;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getResultOutcome() {
        return resultOutcome;
    }

    public void setResultOutcome(String resultOutcome) {
        this.resultOutcome = resultOutcome;
    }
}
