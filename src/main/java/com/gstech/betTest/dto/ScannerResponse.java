package com.gstech.betTest.dto;

public class ScannerResponse {

    // Derived Stats
    private double homeAvgScored;
    private double homeAvgConceded;
    private double awayAvgScored;
    private double awayAvgConceded;

    // Percentage of Over 1.5 in last 5 games
    private double homeOverPercent;
    private double awayOverPercent;

    // Predictions
    private double expectedTotalGoals;
    private double probabilityOver15;
    private double fairOddOver15;

    // Analysis
    private double marketOdd;
    private boolean isValueBet;
    private String confidenceLevel; // LOW, MEDIUM, HIGH

    // New Markets - Team Goals
    private double probabilityBTTS;
    private double homeProbOver05;
    private double homeProbOver15;
    private double awayProbOver05;
    private double awayProbOver15;

    public ScannerResponse(double homeAvgScored, double homeAvgConceded, double awayAvgScored, double awayAvgConceded,
            double homeOverPercent, double awayOverPercent, double expectedTotalGoals, double probabilityOver15,
            double fairOddOver15, double marketOdd, boolean isValueBet, String confidenceLevel, double probabilityBTTS,
            double homeProbOver05, double homeProbOver15, double awayProbOver05, double awayProbOver15) {
        this.homeAvgScored = homeAvgScored;
        this.homeAvgConceded = homeAvgConceded;
        this.awayAvgScored = awayAvgScored;
        this.awayAvgConceded = awayAvgConceded;
        this.homeOverPercent = homeOverPercent;
        this.awayOverPercent = awayOverPercent;
        this.expectedTotalGoals = expectedTotalGoals;
        this.probabilityOver15 = probabilityOver15;
        this.fairOddOver15 = fairOddOver15;
        this.marketOdd = marketOdd;
        this.isValueBet = isValueBet;
        this.confidenceLevel = confidenceLevel;
        this.probabilityBTTS = probabilityBTTS;
        this.homeProbOver05 = homeProbOver05;
        this.homeProbOver15 = homeProbOver15;
        this.awayProbOver05 = awayProbOver05;
        this.awayProbOver15 = awayProbOver15;
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

    public double getProbabilityBTTS() {
        return probabilityBTTS;
    }

    public void setProbabilityBTTS(double probabilityBTTS) {
        this.probabilityBTTS = probabilityBTTS;
    }

    public double getHomeProbOver05() {
        return homeProbOver05;
    }

    public void setHomeProbOver05(double homeProbOver05) {
        this.homeProbOver05 = homeProbOver05;
    }

    public double getHomeProbOver15() {
        return homeProbOver15;
    }

    public void setHomeProbOver15(double homeProbOver15) {
        this.homeProbOver15 = homeProbOver15;
    }

    public double getAwayProbOver05() {
        return awayProbOver05;
    }

    public void setAwayProbOver05(double awayProbOver05) {
        this.awayProbOver05 = awayProbOver05;
    }

    public double getAwayProbOver15() {
        return awayProbOver15;
    }

    public void setAwayProbOver15(double awayProbOver15) {
        this.awayProbOver15 = awayProbOver15;
    }
}
