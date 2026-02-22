package com.gstech.betTest.repository;

import com.gstech.betTest.model.SavedBet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedBetRepository extends JpaRepository<SavedBet, Long> {
}
