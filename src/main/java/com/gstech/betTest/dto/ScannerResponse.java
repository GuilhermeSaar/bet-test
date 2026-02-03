package com.gstech.betTest.dto;

public class ScannerResponse {

    // Derived Stats
    private double homeAvgScored;
    private double homeAvgConceded;
    private double awayAvgScored;
    private double awayAvgConceded;

    // Percentage of Over 2.5 in last 5 games
    private double homeOverPercent;
    private double awayOverPercent;

    // Predictions
    private double expectedTotalGoals;
    private double probabilityOver25;
    private double fairOddOver25;

    // Analysis
    private double marketOdd;
    private boolean isValueBet;
    private String confidenceLevel; // LOW, MEDIUM, HIGH

    public ScannerResponse(double homeAvgScored, double homeAvgConceded, double awayAvgScored, double awayAvgConceded, double homeOverPercent, double awayOverPercent, double expectedTotalGoals, double probabilityOver25, double fairOddOver25, double marketOdd, boolean isValueBet, String confidenceLevel) {
        this.homeAvgScored = homeAvgScored;
        this.homeAvgConceded = homeAvgConceded;
        this.awayAvgScored = awayAvgScored;
        this.awayAvgConceded = awayAvgConceded;
        this.homeOverPercent = homeOverPercent;
        this.awayOverPercent = awayOverPercent;
        this.expectedTotalGoals = expectedTotalGoals;
        this.probabilityOver25 = probabilityOver25;
        this.fairOddOver25 = fairOddOver25;
        this.marketOdd = marketOdd;
        this.isValueBet = isValueBet;
        this.confidenceLevel = confidenceLevel;
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

    public double getHomeOverPercent() {
        return homeOverPercent;
    }

    public void setHomeOverPercent(double homeOverPercent) {
        this.homeOverPercent = homeOverPercent;
    }

    public double getAwayOverPercent() {
        return awayOverPercent;
    }

    public void setAwayOverPercent(double awayOverPercent) {
        this.awayOverPercent = awayOverPercent;
    }

    public double getExpectedTotalGoals() {
        return expectedTotalGoals;
    }

    public void setExpectedTotalGoals(double totalXG) {
        this.expectedTotalGoals = totalXG;
    }

    public double getProbabilityOver25() {
        return probabilityOver25;
    }

    public void setProbabilityOver25(double probabilityOver25) {
        this.probabilityOver25 = probabilityOver25;
    }

    public double getFairOddOver25() {
        return fairOddOver25;
    }

    public void setFairOddOver25(double fairOddOver25) {
        this.fairOddOver25 = fairOddOver25;
    }

    public double getMarketOdd() {
        return marketOdd;
    }

    public void setMarketOdd(double marketOdd) {
        this.marketOdd = marketOdd;
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
}
