package com.gstech.betTest.repository;

import com.gstech.betTest.model.Parlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParlayRepository extends JpaRepository<Parlay, Long> {
}
