package com.gstech.betTest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_parlays")
public class Parlay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalOdd;
    private Double totalProbability;
    private String status = "PENDENTE"; // WIN, LOSS, PENDING

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "parlay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedBet> bets = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Parlay() {
    }

    public void addBet(SavedBet bet) {
        bets.add(bet);
        bet.setParlay(this);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalOdd() {
        return totalOdd;
    }

    public void setTotalOdd(Double totalOdd) {
        this.totalOdd = totalOdd;
    }

    public Double getTotalProbability() {
        return totalProbability;
    }

    public void setTotalProbability(Double totalProbability) {
        this.totalProbability = totalProbability;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SavedBet> getBets() {
        return bets;
    }

    public void setBets(List<SavedBet> bets) {
        this.bets = bets;
    }
}
