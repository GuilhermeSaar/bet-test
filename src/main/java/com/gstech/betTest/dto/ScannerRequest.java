package com.gstech.betTest.dto;

import java.util.List;

public class ScannerRequest {

    private String homeTeam;
    private String awayTeam;
    private List<String> homeLastMatches;
    private List<String> awayLastMatches;
    private Double marketOddOver25;

    // Getters and Setters
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

    public List<String> getHomeLastMatches() {
        return homeLastMatches;
    }

    public void setHomeLastMatches(List<String> homeLastMatches) {
        this.homeLastMatches = homeLastMatches;
    }

    public List<String> getAwayLastMatches() {
        return awayLastMatches;
    }

    public void setAwayLastMatches(List<String> awayLastMatches) {
        this.awayLastMatches = awayLastMatches;
    }

    public Double getMarketOddOver25() {
        return marketOddOver25;
    }

    public void setMarketOddOver25(Double marketOddOver25) {
        this.marketOddOver25 = marketOddOver25;
    }
}
